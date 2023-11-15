package com.modsen.service;

import com.modsen.dto.RideListResponse;
import com.modsen.dto.RideRequest;
import com.modsen.dto.RideResponse;
import com.modsen.model.PageSetting;

public interface RideService {


    RideListResponse getAllRide(PageSetting pageSetting);

    RideResponse getRideById(long id);

    RideResponse createRide(RideRequest rideRequest);

    RideResponse updateRide(long id, RideRequest rideRequest);

    void deleteRide(long id);
}