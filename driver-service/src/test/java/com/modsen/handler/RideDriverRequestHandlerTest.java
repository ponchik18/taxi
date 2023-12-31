package com.modsen.handler;

import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.enums.DriverStatus;
import com.modsen.model.Driver;
import com.modsen.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RideDriverRequestHandlerTest {
    @Mock
    private DriverRepository driverRepository;

    @Mock
    private MessageChannel producingChannel;

    @InjectMocks
    private RideDriverRequestHandler rideDriverRequestHandler;

    @Test
    public void handleMessage_ValidMessage_Success() {
        RideDriverRequest rideDriverRequest = new RideDriverRequest();
        Driver existingDriver = new Driver();
        existingDriver.setId(1L);
        List<Driver> availableDrivers = Collections.singletonList(new Driver());
        when(driverRepository.findAllByDriverStatus(DriverStatus.AVAILABLE)).thenReturn(availableDrivers);

        rideDriverRequestHandler.handleMessage(new GenericMessage<>(rideDriverRequest));

        verify(producingChannel).send(any());
    }

}