package com.modsen.component;

import com.modsen.dto.driver.DriverListResponse;
import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverResponse;
import com.modsen.enums.DriverStatus;
import com.modsen.model.Driver;
import com.modsen.model.PageSetting;
import com.modsen.repository.DriverRepository;
import com.modsen.service.impl.DriverServiceImpl;
import com.modsen.util.PageRequestFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DriversComponentStepDefinitions {

    @Mock
    private DriverRepository driverRepository;
    @InjectMocks
    private DriverServiceImpl driverService;
    private DriverListResponse driverListResponse;
    private DriverResponse driverResponse;

    @SuppressWarnings("resource")
    @Given("The driver service business logic is available")
    public void thePassengerServiceBusinessLogicIsAvailable() {
        MockitoAnnotations.openMocks(this);
    }


    @When("The business logic is executed to retrieve all drivers")
    public void theBusinessLogicIsExecutedToRetrieveAllDrivers() {
        PageSetting pageSetting = new PageSetting();
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);
        Driver driver = Driver.builder()
                .driverStatus(DriverStatus.BUSY)
                .build();
        List<Driver> expectedPassengers = Collections.singletonList(driver);
        Page<Driver> page = new PageImpl<>(expectedPassengers);

        when(driverRepository.findAll(pageable))
                .thenReturn(page);

        driverListResponse = driverService.getAllDrivers(pageSetting);
    }

    @Then("The response should contain a list of driver")
    public void thenTheResponseShouldContainAListOfDrivers() {
        assertThat(driverListResponse).isNotNull();
        assertThat(driverListResponse.driverCount()).isGreaterThan(0);
    }

    @When("A new driver is created with first name {string}, last name {string}, email {string}, and phone {string}, and licenseNumber {string}")
    public void whenNewDriversIsCreatedWithFirstNameLastNameEmailAndPhone(String firstName, String lastName, String email, String phone, String licenseNumber) {
        Driver passenger = Driver.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .email(email)
                .id(1L)
                .driverStatus(DriverStatus.AVAILABLE)
                .licenseNumber(licenseNumber)
                .build();
        DriverRequest driverRequest = new DriverRequest(licenseNumber, firstName, lastName, email, phone, DriverStatus.AVAILABLE.name());

        when(driverRepository.save(any())).thenReturn(passenger);

        driverResponse = driverService.createDriver(driverRequest);
    }

    @Then("Method save should be invoked")
    public void thenTheResponseShouldContainTheDetailsOfTheNewDriver() {
        verify(driverRepository, times(1)).save(any());
        assertThat(driverResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(driverResponse);

    }

    @When("The business logic is executed to retrieve a driver by ID {long}")
    public void whenTheBusinessLogicIsExecutedToRetrieveADriverByID(long driverId) {
        Driver driver = Driver.builder()
                .driverStatus(DriverStatus.BUSY)
                .build();

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(driver));

        driverResponse = driverService.getDriverById(driverId);
    }

    @Then("The response should contain the details of the specified driver")
    public void thenTheResponseShouldContainTheDetailsOfTheSpecifiedDriver() {
        verify(driverRepository, times(1))
                .findById(any());
        assertThat(driverResponse).isNotNull();
    }

    @When("The business logic is executed to delete a driver by ID")
    public void theBusinessLogicIsExecutedToDeleteADriverByID() {
        when(driverRepository.existsById(any())).thenReturn(true);
    }

    @Then("The driver should be deleted from the database")
    public void thenTheDriverShouldBeDeletedFromTheDatabase() {
        long driverId = 999L;
        assertDoesNotThrow(() -> driverService.deleteDriver(driverId));
        verify(driverRepository, times(1)).deleteById(driverId);
    }
}