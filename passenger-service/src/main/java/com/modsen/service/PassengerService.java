package com.modsen.service;

import com.modsen.dto.passenger.PassengerListResponse;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.model.PageSetting;


public interface PassengerService {

    PassengerListResponse getAllPassenger(PageSetting pageSetting);

    PassengerResponse createPassenger(PassengerRequest passengerRequest);

    PassengerResponse getPassengerById(long id);

    PassengerResponse updatePassenger(long id, PassengerRequest passengerRequest);

    void deletePassenger(long id);
}