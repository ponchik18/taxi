package com.modsen.controller;

import com.modsen.constants.DriverServiceConstants;
import com.modsen.dto.driver.DriverListResponse;
import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverResponse;
import com.modsen.dto.driver.DriverStatusChangeRequest;
import com.modsen.model.PageSetting;
import com.modsen.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DriverServiceConstants.Path.DRIVER_CONTROLLER_PATH)
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public DriverListResponse getAllDriver(PageSetting pageSetting) {
        return driverService.getAllDrivers(pageSetting);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DRIVER')")
    public DriverResponse createDriver(@Valid @RequestBody DriverRequest driverRequest) {
        return driverService.createDriver(driverRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DRIVER')")
    public DriverResponse getDriverById(@PathVariable long id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DRIVER')")
    public DriverResponse updateDriver(@PathVariable long id, @Valid @RequestBody DriverRequest driverRequest) {
        return driverService.updateDriver(id, driverRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDriver(@PathVariable long id) {
        driverService.deleteDriver(id);
    }

    @PatchMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DRIVER')")
    public DriverResponse changeStatus(@RequestBody DriverStatusChangeRequest driverStatusChangeRequest) {
        return driverService.changeStatus(driverStatusChangeRequest);
    }
}