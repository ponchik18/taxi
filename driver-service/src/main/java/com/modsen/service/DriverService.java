package com.modsen.service;

import com.modsen.dto.DriverListResponse;
import com.modsen.dto.DriverRequest;
import com.modsen.dto.DriverResponse;
import com.modsen.model.PageSetting;

public interface DriverService {
    DriverListResponse getAllDrivers(PageSetting pageSetting);
    DriverResponse getDriverById(long id);
    DriverResponse createDriver(DriverRequest driverRequest);
    DriverResponse updateDriver(long id, DriverRequest driverRequest);
    void deleteDriver(long id);
}