package com.modsen.handler;

import com.modsen.dto.rides.RideResponseWithDriver;
import com.modsen.mapper.RideMapper;
import com.modsen.model.Ride;
import com.modsen.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.time.LocalDateTime;

public class RideResponseWithDriverHandler implements MessageHandler {

    private RideRepository rideRepository;

    @Autowired
    public void setRideRepository(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {
        RideResponseWithDriver rideResponseWithDriver = (RideResponseWithDriver) message.getPayload();

        Ride ride = RideMapper.MAPPER_INSTANCE.mapToRide(rideResponseWithDriver.rideDriverRequest());
        ride.setStartTime(LocalDateTime.now());
        ride.setIsPromoCodeApplied(false);
        ride.setDriverId(rideResponseWithDriver.driverId());
        rideRepository.save(ride);
    }
}