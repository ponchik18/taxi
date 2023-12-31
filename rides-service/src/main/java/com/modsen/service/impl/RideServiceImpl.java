package com.modsen.service.impl;

import com.modsen.dto.driver.DriverChangeStatusForKafkaRequest;
import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.promo.PromoCodeResponse;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RideListResponse;
import com.modsen.dto.rides.RidePassengerRequest;
import com.modsen.dto.rides.RideResponse;
import com.modsen.enums.DriverStatus;
import com.modsen.enums.RideStatus;
import com.modsen.exception.PromoCodeAlreadyAppliedException;
import com.modsen.exception.PromoCodeNotFoundException;
import com.modsen.exception.RideCancelException;
import com.modsen.exception.RideNotFoundException;
import com.modsen.mapper.RideMapper;
import com.modsen.model.PageSetting;
import com.modsen.model.Ride;
import com.modsen.repository.RideRepository;
import com.modsen.service.PassengerServiceWebClient;
import com.modsen.service.PaymentServiceWebClient;
import com.modsen.service.PromoCodeServiceWebClient;
import com.modsen.service.RideService;
import com.modsen.util.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final MessageChannel producingChannel;
    private final PassengerServiceWebClient passengerServiceWebClient;
    private final PaymentServiceWebClient paymentServiceWebClient;
    private final PromoCodeServiceWebClient promoCodeServiceWebClient;
    private final KafkaTemplate<String, Object> customKafkaTemplate;

    @Value("${spring.integration.kafka.sent-topic}")
    private String springIntegrationKafkaAcceptedTopic;
    @Value("${spring.kafka.producer.change-driver-status-topic}")
    private String springKafkaChangeDriverStatus;

    @Override
    public RideListResponse getAllRide(PageSetting pageSetting, Long passengerId) {
        if (Objects.nonNull(passengerId)) {
            List<Ride> rides = rideRepository.findAllByPassengerId(passengerId);
            return RideListResponse.builder()
                    .rides(RideMapper.MAPPER_INSTANCE.mapToListOfRideResponse(rides))
                    .ridesCount(rides.size())
                    .build();
        }
        PageRequest pageRequest = PageRequestFactory.buildPageRequest(pageSetting);
        List<Ride> rides = rideRepository.findAll(pageRequest)
                .toList();
        return RideListResponse.builder()
                .rides(RideMapper.MAPPER_INSTANCE.mapToListOfRideResponse(rides))
                .ridesCount(rides.size())
                .build();
    }

    @Override
    public RideResponse getRideById(long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(ride);
    }

    @Override
    public void createRide(RidePassengerRequest ridePassengerRequest) {
        validatePassenger(ridePassengerRequest.getPassengerId());
        RideDriverRequest rideDriverRequest = RideDriverRequest.builder()
                .cost(generateCost())
                .dropLocation(ridePassengerRequest.getDropLocation())
                .pickUpLocation(ridePassengerRequest.getPickUpLocation())
                .passengerId(ridePassengerRequest.getPassengerId())
                .status(RideStatus.DRIVER_EN_ROUTE.name())
                .build();

        producingChannel.send(new GenericMessage<>(
                rideDriverRequest,
                Collections.singletonMap(KafkaHeaders.TOPIC, springIntegrationKafkaAcceptedTopic))
        );
    }

    @Override
    public RideResponse updateRide(long id, RideDriverRequest rideDriverRequest) {
        if (!rideRepository.existsById(id)) {
            throw new RideNotFoundException(id);
        }

        Ride updatedRide = RideMapper.MAPPER_INSTANCE.mapToRide(rideDriverRequest);
        updatedRide.setId(id);
        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(
                rideRepository.save(updatedRide)
        );
    }

    @Override
    public void deleteRide(long id) {
        if (!rideRepository.existsById(id)) {
            throw new RideNotFoundException(id);
        }

        rideRepository.deleteById(id);
    }

    @Override
    public RideResponse cancelRide(ChangeRideStatusRequest changeRideStatusRequest) {
        Ride ride = getRideByChangeRideStatusRequest(changeRideStatusRequest);
        if (ride.getStatus() != RideStatus.DRIVER_EN_ROUTE) {
            throw new RideCancelException(ride.getId(), ride.getStatus());
        }
        customKafkaTemplate.send(
                springKafkaChangeDriverStatus,
                new DriverChangeStatusForKafkaRequest(ride.getDriverId(), DriverStatus.AVAILABLE)
        );
        ride.setStatus(RideStatus.TRIP_CANCELED);
        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(
                rideRepository.save(ride)
        );
    }

    @Override
    public RideResponse confirmDriverArrival(ChangeRideStatusRequest changeRideStatusRequest) {
        Ride ride = getRideByChangeRideStatusRequest(changeRideStatusRequest);
        if (ride.getStatus() == RideStatus.DRIVER_EN_ROUTE) {
            ride.setStatus(RideStatus.IN_PROGRESS);
        }
        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(
                rideRepository.save(ride)
        );
    }

    @Override
    public RideResponse finishRide(ChangeRideStatusRequest changeRideStatusRequest) {
        Ride ride = getRideByChangeRideStatusRequest(changeRideStatusRequest);
        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            return RideMapper.MAPPER_INSTANCE.mapToRideResponse(ride);
        }
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .rideId(ride.getId())
                .passengerId(ride.getPassengerId())
                .driverId(ride.getDriverId())
                .amount(ride.getCost())
                .build();
        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());

        paymentServiceWebClient.makeCharge(paymentRequest);
        customKafkaTemplate.send(
                springKafkaChangeDriverStatus,
                new DriverChangeStatusForKafkaRequest(ride.getDriverId(), DriverStatus.AVAILABLE)
        );

        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(
                rideRepository.save(ride)
        );
    }

    @Override
    public RideResponse applyApplyCode(PromoCodeApplyRequest promoCodeApplyRequest) {
        Ride ride = rideRepository.findByIdAndPassengerId(promoCodeApplyRequest.getRideId(), promoCodeApplyRequest.getPassengerId())
                .orElseThrow(() -> new RideNotFoundException(promoCodeApplyRequest.getRideId()));
        if (ride.getIsPromoCodeApplied()) {
            throw new PromoCodeAlreadyAppliedException(promoCodeApplyRequest.getPromoCode());
        }
        PromoCodeResponse promoCodeResponse = promoCodeServiceWebClient.applyPromoCodeForRide(promoCodeApplyRequest)
                .orElseThrow(() -> new PromoCodeNotFoundException(promoCodeApplyRequest.getPromoCode()));

        ride.setIsPromoCodeApplied(true);
        BigDecimal newPriceForRide = ride.getCost()
                .subtract(ride.getCost().multiply(BigDecimal.valueOf(promoCodeResponse.discount()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        ride.setCost(newPriceForRide);

        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(rideRepository.save(ride));
    }

    private BigDecimal generateCost() {
        return BigDecimal.valueOf(Math.random() * 100 + 1)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Ride getRideByChangeRideStatusRequest(ChangeRideStatusRequest changeRideStatusRequest) {
        return rideRepository.findByIdAndDriverId(
                changeRideStatusRequest.getRideId(),
                changeRideStatusRequest.getDriverId()
        ).orElseThrow(() -> new RideNotFoundException(changeRideStatusRequest.getRideId()));
    }

    private void validatePassenger(Long passengerId) {
        passengerServiceWebClient.getPassengerById(passengerId);

        paymentServiceWebClient.getDefaultCardForPassenger(passengerId);
    }
}