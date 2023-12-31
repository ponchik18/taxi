package com.modsen.component;

import com.modsen.dto.passenger.PassengerListResponse;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.model.PageSetting;
import com.modsen.model.Passenger;
import com.modsen.repository.PassengerRepository;
import com.modsen.service.impl.PassengerServiceImpl;
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

public class PassengerComponentStepDefinitions {

    @Mock
    private PassengerRepository passengerRepository;
    @InjectMocks
    private PassengerServiceImpl passengerService;
    private PassengerListResponse passengerListResponse;
    private PassengerResponse passengerResponse;

    @SuppressWarnings("resource")
    @Given("The passenger service business logic is available")
    public void thePassengerServiceBusinessLogicIsAvailable() {
        MockitoAnnotations.openMocks(this);
    }


    @When("The business logic is executed to retrieve all passengers")
    public void theBusinessLogicIsExecutedToRetrieveAllPassengers() {
        PageSetting pageSetting = new PageSetting();
        Pageable pageable = PageRequestFactory.buildPageRequest(pageSetting);

        List<Passenger> expectedPassengers = Collections.singletonList(new Passenger());
        Page<Passenger> page = new PageImpl<>(expectedPassengers);

        when(passengerRepository.findAll(pageable))
                .thenReturn(page);

        passengerListResponse = passengerService.getAllPassenger(pageSetting);
    }

    @Then("The response should contain a list of passengers")
    public void thenTheResponseShouldContainAListOfPassengers() {
        assertThat(passengerListResponse).isNotNull();
        assertThat(passengerListResponse.passengerCount()).isGreaterThan(0);
    }

    @When("A new passenger is created with first name {string}, last name {string}, email {string}, and phone {string}")
    public void whenNewPassengerIsCreatedWithFirstNameLastNameEmailAndPhone(String firstName, String lastName, String email, String phone) {
        Passenger passenger = Passenger.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .email(email)
                .id(1L)
                .build();
        PassengerRequest passengerRequest = new PassengerRequest(firstName, lastName, email, phone);

        when(passengerRepository.save(any())).thenReturn(passenger);

        passengerResponse = passengerService.createPassenger(passengerRequest);
    }

    @Then("Method save should be invoked")
    public void thenTheResponseShouldContainTheDetailsOfTheNewPassenger() {
        verify(passengerRepository, times(1)).save(any());
        assertThat(passengerResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(passengerResponse);

    }

    @When("The business logic is executed to retrieve a passenger by ID {long}")
    public void whenTheBusinessLogicIsExecutedToRetrieveAPassengerByID(long passengerId) {
        when(passengerRepository.findById(passengerId))
                .thenReturn(Optional.of(new Passenger()));

        passengerResponse = passengerService.getPassengerById(passengerId);
    }

    @Then("The response should contain the details of the specified passenger")
    public void thenTheResponseShouldContainTheDetailsOfTheSpecifiedPassenger() {
        verify(passengerRepository, times(1))
                .findById(any());
        assertThat(passengerResponse).isNotNull();
    }

    @When("The business logic is executed to delete a passenger by ID")
    public void theBusinessLogicIsExecutedToDeleteAPassengerByID() {
        when(passengerRepository.existsById(any())).thenReturn(true);
    }

    @Then("The passenger should be deleted from the database")
    public void thenThePassengerShouldBeDeletedFromTheDatabase() {
        long passengerId = 999L;
        assertDoesNotThrow(() -> passengerService.deletePassenger(passengerId));
        verify(passengerRepository, times(1)).deleteById(passengerId);
    }
}