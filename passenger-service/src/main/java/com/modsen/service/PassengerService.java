package com.modsen.service;

import com.modsen.dto.PassengerRequest;
import com.modsen.dto.PassengerResponse;
import com.modsen.model.PageSetting;

import java.util.List;


public interface PassengerService {

    List<PassengerResponse> getAllPassenger(PageSetting pageSetting);

    void createPassenger(PassengerRequest passengerRequest);

    PassengerResponse getPassengerById(Integer id);

    void updatePassenger(Integer id, PassengerRequest passengerRequest);

    void deletePassenger(Integer id);
}