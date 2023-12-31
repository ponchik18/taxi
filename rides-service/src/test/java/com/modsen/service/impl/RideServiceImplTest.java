package com.modsen.service.impl;

import com.modsen.constants.RidesServiceConstants;
import com.modsen.constants.RidesServiceTestConstants;
import com.modsen.dto.card.CreditCardResponse;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.promo.PromoCodeResponse;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RideListResponse;
import com.modsen.dto.rides.RidePassengerRequest;
import com.modsen.dto.rides.RideResponse;
import com.modsen.enums.RideStatus;
import com.modsen.exception.PromoCodeAlreadyAppliedException;
import com.modsen.exception.PromoCodeNotFoundException;
import com.modsen.exception.RideCancelException;
import com.modsen.exception.RideNotFoundException;
import com.modsen.model.PageSetting;
import com.modsen.model.Ride;
import com.modsen.repository.RideRepository;
import com.modsen.service.PassengerServiceWebClient;
import com.modsen.service.PaymentServiceWebClient;
import com.modsen.service.PromoCodeServiceWebClient;
import com.modsen.util.PageRequestFactory;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RideServiceImplTest {

    private final List<Ride> rideList = new ArrayList<>();
    @Mock
    private RideRepository rideRepository;
    @Mock
    private MessageChannel producingChannel;
    @Mock
    private PassengerServiceWebClient passengerServiceWebClient;
    @Mock
    private PaymentServiceWebClient paymentServiceWebClient;
    @Mock
    private PromoCodeServiceWebClient promoCodeServiceWebClient;
    @Mock
    private KafkaTemplate<String, Object> customKafkaTemplate;
    @InjectMocks
    private RideServiceImpl rideService;
    private RidePassengerRequest ridePassengerRequest;
    private RideDriverRequest rideDriverRequest;

    @BeforeEach
    void setUp() {
        rideList.add(Ride.builder()
                .driverId(RidesServiceTestConstants.TestData.DRIVER_ID_1)
                .passengerId(RidesServiceTestConstants.TestData.PASSENGER_ID)
                .cost(BigDecimal.TEN)
                .status(RideStatus.IN_PROGRESS)
                .isPromoCodeApplied(false)
                .build());
        rideList.add(Ride.builder()
                .driverId(RidesServiceTestConstants.TestData.DRIVER_ID_1)
                .passengerId(RidesServiceTestConstants.TestData.PASSENGER_ID)
                .cost(BigDecimal.TEN)
                .status(RideStatus.DRIVER_EN_ROUTE)
                .isPromoCodeApplied(false)
                .build());

        ridePassengerRequest = new RidePassengerRequest();
        ridePassengerRequest.setPassengerId(RidesServiceTestConstants.TestData.PASSENGER_ID);

        rideDriverRequest = new RideDriverRequest(RidesServiceTestConstants.TestData.PASSENGER_ID, RidesServiceTestConstants.TestData.START_POINT, RidesServiceTestConstants.TestData.END_POINT, LocalDateTime.now(), LocalDateTime.now(), BigDecimal.TEN, RideStatus.COMPLETED.name());
    }

    @Test
    public void getAllRide_ValidPageSettingAndPassengerId_Success() {
        when(rideRepository.findAllByPassengerId(RidesServiceTestConstants.TestData.PASSENGER_ID))
                .thenReturn(rideList);

        RideListResponse actualRideListResponse = rideService.getAllRide(new PageSetting(), RidesServiceTestConstants.TestData.PASSENGER_ID);

        verify(rideRepository, times(1)).findAllByPassengerId(RidesServiceTestConstants.TestData.PASSENGER_ID);
        assertThat(actualRideListResponse.ridesCount()).isEqualTo(rideList.size());
    }

    @Test
    public void getAllRide_ValidPageSetting_Success() {
        PageSetting pageSetting = new PageSetting();
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);
        Page<Ride> page = new PageImpl<>(rideList);

        when(rideRepository.findAll(pageable))
                .thenReturn(page);

        RideListResponse actualRideListResponse = rideService.getAllRide(new PageSetting(), null);

        verify(rideRepository, times(1)).findAll(any(Pageable.class));
        assertThat(actualRideListResponse.ridesCount()).isEqualTo(rideList.size());
    }

    @Test
    void getPassengerById_ExistingId_Success() {
        long rideId = 1L;

        when(rideRepository.findById(rideId))
                .thenReturn(Optional.of(rideList.get(0)));
        RideResponse actual = rideService.getRideById(rideId);

        assertNotNull(actual);
    }

    @Test
    void getPassengerById_NonExistingId_ExceptionThrown() {
        long rideId = 1L;
        String exceptedMessage = String.format(RidesServiceConstants.Errors.Message.RIDE_NOT_FOUND, rideId);

        when(rideRepository.findById(rideId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.getRideById(rideId))
                .isInstanceOf(RideNotFoundException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    public void createRide_ValidRidePassengerRequest_Success() {
        when(passengerServiceWebClient.getPassengerById(any(Long.class)))
                .thenReturn(Optional.ofNullable(PassengerResponse.builder().build()));
        when(paymentServiceWebClient.getDefaultCardForPassenger(any(Long.class)))
                .thenReturn(Optional.ofNullable(CreditCardResponse.builder().build()));

        rideService.createRide(ridePassengerRequest);

        verify(producingChannel, times(1)).send(any(GenericMessage.class));
    }

    @Test
    public void createRide_UnExistPassenger_Success() {
        when(passengerServiceWebClient.getPassengerById(any(Long.class)))
                .thenThrow(ResourceNotFoundException.class);

        assertThatThrownBy(() -> rideService.createRide(ridePassengerRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void createRide_UnExistDefaultCreditCard_Success() {
        when(passengerServiceWebClient.getPassengerById(any(Long.class)))
                .thenReturn(Optional.ofNullable(PassengerResponse.builder().build()));
        when(paymentServiceWebClient.getDefaultCardForPassenger(any(Long.class)))
                .thenThrow(ResourceNotFoundException.class);

        assertThatThrownBy(() -> rideService.createRide(ridePassengerRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateRide_ValidRideRequestAndExistingId_Success() {
        long rideId = 1L;

        when(rideRepository.existsById(rideId))
                .thenReturn(true);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(rideList.get(0));
        RideResponse actualRideResponse = rideService.updateRide(rideId, rideDriverRequest);

        assertNotNull(actualRideResponse);
        ArgumentCaptor<Ride> rideArgumentCaptor = ArgumentCaptor.forClass(Ride.class);
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        Ride actualRide = rideArgumentCaptor.getValue();
        assertThat(actualRide).usingRecursiveComparison()
                .ignoringFields("id", "isPromoCodeApplied", "status", "driverId")
                .isEqualTo(rideDriverRequest);
    }

    @Test
    public void updateRide_ValidRideRequestAndUnExistingId_ThrowException() {
        long rideId = 1L;
        String exceptedMessage = String.format(RidesServiceConstants.Errors.Message.RIDE_NOT_FOUND, rideId);

        assertThatThrownBy(() -> rideService.updateRide(rideId, new RideDriverRequest()))
                .isInstanceOf(RideNotFoundException.class)
                .hasMessageContaining(exceptedMessage);
    }

    @Test
    public void deleteRide_ExistingId_Success() {
        long rideId = 1L;

        when(rideRepository.existsById(rideId)).thenReturn(true);

        assertDoesNotThrow(() -> rideService.deleteRide(rideId));
        verify(rideRepository, times(1)).deleteById(rideId);
    }

    @Test
    public void deleteRide_UnExistingId_ThrowException() {
        long rideId = 1L;
        String expectedMessage = String.format(RidesServiceConstants.Errors.Message.RIDE_NOT_FOUND, rideId);

        when(rideRepository.existsById(rideId)).thenReturn(false);

        assertThatThrownBy(() -> rideService.deleteRide(rideId))
                .isInstanceOf(RideNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void cancelRide_ValidChangeRideStatusRequest_Success() {
        Ride expectedRide = rideList.get(1);
        expectedRide.setId(RidesServiceTestConstants.TestData.RIDE_ID);
        ChangeRideStatusRequest changeRideStatusRequest = new ChangeRideStatusRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.DRIVER_ID_1);

        when(rideRepository.findByIdAndDriverId(any(), any()))
                .thenReturn(Optional.of(expectedRide));
        rideService.cancelRide(changeRideStatusRequest);

        verify(customKafkaTemplate).send(any(), any());
        ArgumentCaptor<Ride> rideArgumentCaptor = ArgumentCaptor.forClass(Ride.class);
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());

        Ride actualRide = rideArgumentCaptor.getValue();
        expectedRide.setStatus(RideStatus.TRIP_CANCELED);
        assertThat(actualRide)
                .isEqualTo(expectedRide);
    }

    @Test
    public void cancelRide_RideCannotBeCanceled_ThrowException() {
        Ride existingRide = rideList.get(0);
        existingRide.setId(RidesServiceTestConstants.TestData.RIDE_ID);
        ChangeRideStatusRequest changeRideStatusRequest = new ChangeRideStatusRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.DRIVER_ID_1);
        String expectedMessage = String.format(RidesServiceConstants.Errors.Message.RIDE_CAN_NOT_BE_CANCELED, existingRide.getId(), existingRide.getStatus().name());


        when(rideRepository.findByIdAndDriverId(any(), any()))
                .thenReturn(Optional.of(existingRide));

        assertThatThrownBy(() -> rideService.cancelRide(changeRideStatusRequest))
                .isInstanceOf(RideCancelException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void confirmDriverArrival_ValidChangeRideStatusRequest_Success() {
        Ride expectedRide = rideList.get(1);
        expectedRide.setId(RidesServiceTestConstants.TestData.RIDE_ID);
        ChangeRideStatusRequest changeRideStatusRequest = new ChangeRideStatusRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.DRIVER_ID_1);

        when(rideRepository.findByIdAndDriverId(any(), any()))
                .thenReturn(Optional.of(expectedRide));
        rideService.confirmDriverArrival(changeRideStatusRequest);

        ArgumentCaptor<Ride> rideArgumentCaptor = ArgumentCaptor.forClass(Ride.class);
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());

        Ride actualRide = rideArgumentCaptor.getValue();
        expectedRide.setStatus(RideStatus.IN_PROGRESS);
        assertThat(actualRide)
                .isEqualTo(expectedRide);
    }

    @Test
    public void finishRide_ValidChangeRideStatusRequest_Success() {
        Ride expectedRide = rideList.get(0);
        expectedRide.setId(RidesServiceTestConstants.TestData.RIDE_ID);
        ChangeRideStatusRequest changeRideStatusRequest = new ChangeRideStatusRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.DRIVER_ID_1);

        when(rideRepository.findByIdAndDriverId(any(), any()))
                .thenReturn(Optional.of(expectedRide));
        rideService.finishRide(changeRideStatusRequest);

        verify(paymentServiceWebClient).makeCharge(any());
        verify(customKafkaTemplate).send(any(), any());
        ArgumentCaptor<Ride> rideArgumentCaptor = ArgumentCaptor.forClass(Ride.class);
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());

        Ride actualRide = rideArgumentCaptor.getValue();
        expectedRide.setStatus(RideStatus.COMPLETED);
        assertThat(actualRide)
                .isEqualTo(expectedRide);
    }

    @Test
    public void finishRide_InValidChangeRideStatusRequest_NotChange() {
        Ride expectedRide = rideList.get(1);
        expectedRide.setId(RidesServiceTestConstants.TestData.RIDE_ID);
        ChangeRideStatusRequest changeRideStatusRequest = new ChangeRideStatusRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.DRIVER_ID_1);

        when(rideRepository.findByIdAndDriverId(any(), any()))
                .thenReturn(Optional.of(expectedRide));
        RideResponse actualRideResponse = rideService.finishRide(changeRideStatusRequest);

        assertThat(actualRideResponse).usingRecursiveComparison()
                .ignoringFields("status")
                .isEqualTo(expectedRide);
    }


    @Test
    public void applyApplyCode_ValidPromoCodeApplyRequest_Success() {
        PromoCodeApplyRequest promoCodeApplyRequest = new PromoCodeApplyRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.PASSENGER_ID, RidesServiceTestConstants.TestData.PROMO_CODE_NAME);
        Ride existingRide = rideList.get(0);
        BigDecimal expectedPrice = BigDecimal.valueOf(8);
        PromoCodeResponse promoCodeResponse = PromoCodeResponse.builder()
                .id(5L)
                .name(promoCodeApplyRequest.getPromoCode())
                .endDate(LocalDate.now())
                .fromDate(LocalDate.now())
                .countOfUse(2)
                .discount(RidesServiceTestConstants.TestData.PROMO_CODE_DISCOUNT)
                .build();

        when(rideRepository.findByIdAndPassengerId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(existingRide));
        when(promoCodeServiceWebClient.applyPromoCodeForRide(promoCodeApplyRequest))
                .thenReturn(Optional.of(promoCodeResponse));
        rideService.applyApplyCode(promoCodeApplyRequest);

        ArgumentCaptor<Ride> rideArgumentCaptor = ArgumentCaptor.forClass(Ride.class);
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        Ride actualRide = rideArgumentCaptor.getValue();
        assertThat(actualRide.getIsPromoCodeApplied()).isTrue();
        assertThat(actualRide.getCost().compareTo(expectedPrice)).isEqualTo(0);
    }

    @Test
    public void applyApplyCode_PromoCodeAlreadyExist_ThrowException() {
        PromoCodeApplyRequest promoCodeApplyRequest = new PromoCodeApplyRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.PASSENGER_ID, RidesServiceTestConstants.TestData.PROMO_CODE_NAME);
        Ride existingRide = rideList.get(1);
        existingRide.setIsPromoCodeApplied(true);
        String expectedMessage = String.format(RidesServiceConstants.Errors.Message.PROMO_CODE_ALREADY_APPLIED, promoCodeApplyRequest.getPromoCode());
        when(rideRepository.findByIdAndPassengerId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(existingRide));

        assertThatThrownBy(() -> rideService.applyApplyCode(promoCodeApplyRequest))
                .isInstanceOf(PromoCodeAlreadyAppliedException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void applyApplyCode_UnExistingPromoCode_ThrowException() {
        PromoCodeApplyRequest promoCodeApplyRequest = new PromoCodeApplyRequest(RidesServiceTestConstants.TestData.RIDE_ID, RidesServiceTestConstants.TestData.PASSENGER_ID, RidesServiceTestConstants.TestData.PROMO_CODE_NAME);
        Ride existingRide = rideList.get(1);
        String expectedMessage = String.format(RidesServiceConstants.Errors.Message.PROMO_CODE_NOT_FOUND, promoCodeApplyRequest.getPromoCode());
        when(rideRepository.findByIdAndPassengerId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(existingRide));

        assertThatThrownBy(() -> rideService.applyApplyCode(promoCodeApplyRequest))
                .isInstanceOf(PromoCodeNotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }
}