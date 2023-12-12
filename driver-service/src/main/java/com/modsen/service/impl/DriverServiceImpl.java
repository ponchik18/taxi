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
    private final static String SPRING_KAFKA_LISTENER_GROUP_ID = "change-driver-status-listener";
    private final static String SPRING_KAFKA_CHANGE_DRIVER_STATUS_TOPIC = "change-driver-status";


    @Override
    public DriverListResponse getAllDrivers(PageSetting pageSetting) {
        PageRequest pageRequest = PageRequestFactory.buildPageRequest(pageSetting);
        List<Driver> driverList = driverRepository.findAll(pageRequest)
                .toList();
        List<DriverResponse> driverResponseList = DriverMapper.MAPPER_INSTANCE.mapToListOfDriverResponse(driverList);

        return DriverListResponse.builder()
                .drivers(driverResponseList)
                .totalDriversCount(driverResponseList.size())
                .build();
    }

    @Override
    public DriverResponse getDriverById(long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(()->new DriverNotFoundException(id));
        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(driver);
    }

    @Override
    public DriverResponse createDriver(DriverRequest driverRequest) {
        validateDriverRequest(driverRequest);
        Driver driver = DriverMapper.MAPPER_INSTANCE.mapToDriver(driverRequest);
        Driver newDriver = driverRepository.save(driver);
        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(newDriver);
    }

    @Override
    public DriverResponse updateDriver(long id, DriverRequest driverRequest) {
        Driver driver = driverRepository.findById(id)
                        .orElseThrow(()->new DriverNotFoundException(id));
        if(!driver.getEmail().equals(driverRequest.getEmail())) {
            validateDriverRequestByEmail(driverRequest);
        }
        if(!driver.getPhone().equals(driverRequest.getPhone())) {
            validateDriverRequestByPhone(driverRequest);
        }
        if(!driver.getLicenseNumber().equals(driverRequest.getLicenseNumber())) {
            validateDriverRequestByLicenseNumber(driverRequest);
        }

        validateDriverRequest(driverRequest);
        Driver updatedDriver = DriverMapper.MAPPER_INSTANCE.mapToDriver(driverRequest);
        updatedDriver.setId(id);

        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(
                driverRepository.save(updatedDriver)
        );
    }

    @Override
    public void deleteDriver(long id) {
        if(driverRepository.existsById(id)) {
            driverRepository.deleteById(id);
        } else {
            throw new DriverNotFoundException(id);
        }
    }

    @Override
    public DriverResponse changeStatus(DriverStatusChangeRequest driverStatusChangeRequest) {
        Driver driver = driverRepository.findById(driverStatusChangeRequest.getDriverId())
                .orElseThrow(()->new DriverNotFoundException(driverStatusChangeRequest.getDriverId()));
        driver.setDriverStatus(DriverStatus.valueOf(driverStatusChangeRequest.getStatus()));

        return DriverMapper.MAPPER_INSTANCE.mapToDriverResponse(
                driverRepository.save(driver)
        );
    }

    @KafkaListener(topics = SPRING_KAFKA_CHANGE_DRIVER_STATUS_TOPIC, groupId = SPRING_KAFKA_LISTENER_GROUP_ID)
    public void changeDriverStatus(DriverChangeStatusForKafkaRequest driverChangeStatusForKafkaRequest) {
        Driver driver = driverRepository.findById(driverChangeStatusForKafkaRequest.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(driverChangeStatusForKafkaRequest.getDriverId()));
        driver.setDriverStatus(driverChangeStatusForKafkaRequest.getDriverStatus());
        driverRepository.save(driver);
    }

    private void validateDriverRequest(DriverRequest driverRequest) {
        validateDriverRequestByEmail(driverRequest);

        validateDriverRequestByPhone(driverRequest);

        validateDriverRequestByLicenseNumber(driverRequest);

    }

    private void validateDriverRequestByLicenseNumber(DriverRequest driverRequest) {
        if(driverRepository.existsByLicenseNumber(driverRequest.getLicenseNumber())) {
            throw new DuplicateKeyException(
                    String.format(
                            DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_LICENSE_NUMBER,
                            driverRequest.getLicenseNumber()
                    )
            );
        }
    }

    private void validateDriverRequestByPhone(DriverRequest driverRequest) {
        if(driverRepository.existsByPhone(driverRequest.getPhone())) {
            throw new DuplicateKeyException(
                    String.format(
                            DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_PHONE,
                            driverRequest.getPhone()
                    )
            );
        }
    }

    private void validateDriverRequestByEmail(DriverRequest driverRequest) {
        if(driverRepository.existsByEmail(driverRequest.getEmail())) {
            throw new DuplicateKeyException(
                    String.format(
                            DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_EMAIL,
                            driverRequest.getEmail()
                    )
            );
        }
    }
}