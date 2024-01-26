package com.modsen.handler;

import com.modsen.advice.LoggingAdvice;
import com.modsen.dto.rides.RideResponseWithDriver;
import com.modsen.mapper.RideMapper;
import com.modsen.model.Ride;
import com.modsen.repository.RideRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.time.LocalDateTime;

public class RideResponseWithDriverHandler implements MessageHandler {

    private RideRepository rideRepository;
    private static final Logger log = LoggerFactory.getLogger(LoggingAdvice.class);

    @Autowired
    public void setRideRepository(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {
        log.info("Received Message in class RideResponseWithDriverHandler: " + message);
        RideResponseWithDriver rideResponseWithDriver = (RideResponseWithDriver) message.getPayload();

        Ride ride = RideMapper.MAPPER_INSTANCE.mapToRide(rideResponseWithDriver.rideDriverRequest());
        ride.setStartTime(LocalDateTime.now());
        ride.setIsPromoCodeApplied(false);
        ride.setDriverId(rideResponseWithDriver.driverId());
        rideRepository.save(ride);
        log.info("Ride has been created successfully");
    }
}