package com.modsen.service.impl;

import com.modsen.constants.DriverServiceConstants;
import com.modsen.dto.driver.DriverChangeStatusForKafkaRequest;
import com.modsen.dto.driver.DriverListResponse;
import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverResponse;
import com.modsen.dto.driver.DriverStatusChangeRequest;
import com.modsen.enums.DriverStatus;
import com.modsen.exception.DriverNotFoundException;
import com.modsen.mapper.DriverMapper;
import com.modsen.model.Driver;
import com.modsen.model.PageSetting;
import com.modsen.repository.DriverRepository;
import com.modsen.service.DriverService;
import com.modsen.util.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;

    @Override
    public DriverListResponse getAllDrivers(PageSetting pageSetting) {
        PageRequest pageRequest = PageRequestFactory.buildPageRequest(pageSetting);
        List<Driver> driverList = driverRepository.findAll(pageRequest)
                .toList();
        List<DriverResponse> driverResponseList = DriverMapper.MAPPER_INSTANCE.mapToListOfDriverResponse(driverList);

        return DriverListResponse.builder()
                .drivers(driverResponseList)
                .driverCount(driverResponseList.size())
                .build();
    }

    @Override
    public DriverResponse getDriverById(long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(driver);
    }

    @Override
    public DriverResponse createDriver(DriverRequest driverRequest) {
        validateDriverCreateRequest(driverRequest);
        Driver driver = DriverMapper.MAPPER_INSTANCE.mapToDriver(driverRequest);
        Driver newDriver = driverRepository.save(driver);
        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(newDriver);
    }

    @Override
    public DriverResponse updateDriver(long id, DriverRequest driverRequest) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        validateDriverUpdatedRequest(driverRequest, driver);

        Driver updatedDriver = DriverMapper.MAPPER_INSTANCE.mapToDriver(driverRequest);
        updatedDriver.setId(id);

        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(
                driverRepository.save(updatedDriver)
        );
    }

    @Override
    public void deleteDriver(long id) {
        if (!driverRepository.existsById(id)) {
            throw new DriverNotFoundException(id);
        }

        driverRepository.deleteById(id);
    }

    @Override
    public DriverResponse changeStatus(DriverStatusChangeRequest driverStatusChangeRequest) {
        Driver driver = driverRepository.findById(driverStatusChangeRequest.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(driverStatusChangeRequest.getDriverId()));
        driver.setDriverStatus(DriverStatus.valueOf(driverStatusChangeRequest.getStatus()));

        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(
                driverRepository.save(driver)
        );
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void changeDriverStatus(DriverChangeStatusForKafkaRequest driverChangeStatusForKafkaRequest) {
        Driver driver = driverRepository.findById(driverChangeStatusForKafkaRequest.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(driverChangeStatusForKafkaRequest.getDriverId()));
        driver.setDriverStatus(driverChangeStatusForKafkaRequest.getDriverStatus());
        driverRepository.save(driver);
    }

    private void validateDriverUpdatedRequest(DriverRequest driverRequest, Driver driver) {
        if (!driver.getEmail().equals(driverRequest.getEmail())) {
            validateDriverRequestByEmail(driverRequest);
        }
        if (!driver.getPhone().equals(driverRequest.getPhone())) {
            validateDriverRequestByPhone(driverRequest);
        }
        if (!driver.getLicenseNumber().equals(driverRequest.getLicenseNumber())) {
            validateDriverRequestByLicenseNumber(driverRequest);
        }
    }

    private void validateDriverCreateRequest(DriverRequest driverRequest) {
        validateDriverRequestByEmail(driverRequest);

        validateDriverRequestByPhone(driverRequest);

        validateDriverRequestByLicenseNumber(driverRequest);

    }

    private void validateDriverRequestByLicenseNumber(DriverRequest driverRequest) {
        if (driverRepository.existsByLicenseNumber(driverRequest.getLicenseNumber())) {
            String error = String.format(
                    DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_LICENSE_NUMBER,
                    driverRequest.getLicenseNumber()
            );
            throw new DuplicateKeyException(error);
        }
    }

    private void validateDriverRequestByPhone(DriverRequest driverRequest) {
        if (driverRepository.existsByPhone(driverRequest.getPhone())) {
            String error = String.format(
                    DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_PHONE,
                    driverRequest.getPhone()
            );
            throw new DuplicateKeyException(error);
        }
    }

    private void validateDriverRequestByEmail(DriverRequest driverRequest) {
        if (driverRepository.existsByEmail(driverRequest.getEmail())) {
            String error = String.format(
                    DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_EMAIL,
                    driverRequest.getEmail()
            );
            throw new DuplicateKeyException(error);
        }
    }
}