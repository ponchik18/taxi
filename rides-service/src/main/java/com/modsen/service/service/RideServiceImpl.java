package com.modsen.service.service;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;

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
    public RideResponse createRide(RideRequest rideRequest) {
        Ride newRide = RideMapper.MAPPER_INSTANCE.mapToRide(rideRequest);
        Ride savedRide = rideRepository.save(newRide);
        return RideMapper.MAPPER_INSTANCE.mapToRideResponse(savedRide);
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
}