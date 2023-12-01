package com.modsen.service.impl;

import com.modsen.dto.RideListResponse;
import com.modsen.dto.RideRequest;
import com.modsen.dto.RideResponse;
import com.modsen.exception.RideNotFoundException;
import com.modsen.mapper.RideMapper;
import com.modsen.model.PageSetting;
import com.modsen.model.Ride;
import com.modsen.repository.RideRepository;
import com.modsen.service.RideService;
import com.modsen.util.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final MessageChannel producingChannel;

    @Value("${spring.kafka.sent-topic}")
    private String springIntegrationKafkaAcceptedTopic;
    @Override
    public RideListResponse getAllRide(PageSetting pageSetting) {
        PageRequest pageRequest = PageRequestFactory.buildPageRequest(pageSetting);
        List<Ride> rides = rideRepository.findAll(pageRequest)
                .toList();
        return RideListResponse.builder()
                .rides(RideMapper.MAPPER_INSTANCE.mapToListOfRideResponse(rides))
                .totalRidesCount(rides.size())
                .build();
    }

    @Override
    public RideResponse getRideById(long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(()->new RideNotFoundException(id));
        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(ride);
    }

    @Override
    public void createRide(RideRequest rideRequest) {
        producingChannel.send(new GenericMessage<>(
                rideRequest,
                Collections.singletonMap(KafkaHeaders.TOPIC, springIntegrationKafkaAcceptedTopic))
        );
    }

    @Override
    public RideResponse updateRide(long id, RideRequest rideRequest) {
        if(!rideRepository.existsById(id)){
            throw new RideNotFoundException(id);
        }

        Ride updatedRide = RideMapper.MAPPER_INSTANCE.mapToRide(rideRequest);
        updatedRide.setId(id);
        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(
                rideRepository.save(updatedRide)
        );
    }

    @Override
    public void deleteRide(long id) {
        if(!rideRepository.existsById(id)) {
            throw new RideNotFoundException(id);
        }

        rideRepository.deleteById(id);
    }

//    @KafkaListener
//    public void listen(RideRequest rideRequest) {
//        Ride newRide = RideMapper.MAPPER_INSTANCE.mapToRide(rideRequest);
//        Ride savedRide = rideRepository.save(newRide);
//    }
}