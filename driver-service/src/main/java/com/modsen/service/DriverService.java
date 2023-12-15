package com.modsen.service;

import com.modsen.dto.driver.DriverListResponse;
import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverResponse;
import com.modsen.dto.driver.DriverStatusChangeRequest;
import com.modsen.model.PageSetting;

public interface DriverService {
    DriverListResponse getAllDrivers(PageSetting pageSetting);

    DriverResponse getDriverById(long id);

    DriverResponse createDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(long id, DriverRequest driverRequest);

    void deleteDriver(long id);

    DriverResponse changeStatus(DriverStatusChangeRequest driverStatusChangeRequest);
}