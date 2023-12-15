package com.modsen.service;

import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RideListResponse;
import com.modsen.dto.rides.RidePassengerRequest;
import com.modsen.dto.rides.RideResponse;
import com.modsen.model.PageSetting;

public interface RideService {


    RideListResponse getAllRide(PageSetting pageSetting, Long passengerId);

    RideResponse getRideById(long id);

    void createRide(RidePassengerRequest rideRequest);

    RideResponse updateRide(long id, RideDriverRequest rideDriverRequest);

    void deleteRide(long id);

    RideResponse confirmDriverArrival(ChangeRideStatusRequest changeRideStatusRequest);

    RideResponse finishRide(ChangeRideStatusRequest changeRideStatusRequest);

    RideResponse applyApplyCode(PromoCodeApplyRequest promoCodeApplyRequest);

    RideResponse cancelRide(ChangeRideStatusRequest changeRideStatusRequest);
}