package com.modsen;

import com.modsen.client.DriverServiceClient;
import com.modsen.client.RidesServiceClient;
import com.modsen.dto.driver.DriverResponse;
import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RidePassengerRequest;
import com.modsen.dto.rides.RideResponse;
import com.modsen.enums.RideStatus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class EndToEndTestStepDefinitions {
    private static final long RIDE_ID = 1L;
    private static long driverId;
    private final RidesServiceClient rideClient;
    private final DriverServiceClient driverClient;
    private RidePassengerRequest ridePassengerRequest;
    private RideResponse rideResponse;
    private DriverResponse driverResponse;
    private ChangeRideStatusRequest changeRideStatusRequest;
    private BigDecimal costBeforePromoCode;

    @Given("An existing passenger with id {int} creates a ride request with start point {string} and destination point {string}")
    public void anExistingPassengerWithIdCreatesARideRequestWithStartPointAndDestinationPoint(long passengerId, String startPoint, String endPoint) {
        ridePassengerRequest = new RidePassengerRequest(passengerId, startPoint, endPoint);
    }

    @When("A passenger sends this request to the create ride endpoint")
    public void passengerSendsThisRequestToTheCreateRideEndpoint() {
        rideClient.createRide(ridePassengerRequest);
    }

    @Then("A passenger should get details of ride order with status {string}")
    public void aPassengerShouldGetDetailsOfRideOrderWithStatus(String rideStatus) {
        rideResponse = rideClient.getRideById(RIDE_ID);
        assertThat(rideResponse.status())
                .isEqualTo(RideStatus.valueOf(rideStatus));
    }

    @Then("A driver's status should be changed to {string} with id {long}")
    public void aDriverSStatusShouldBeChangedTo(String driverStatus, long driverId) {
        EndToEndTestStepDefinitions.driverId = driverId;
        driverResponse = driverClient.getDriverById(driverId);
        assertThat(driverResponse.driverStatus())
                .isEqualTo(driverStatus);
    }

    @Given("The driver with id {long}")
    public void theRideHasStatus(long driverId) {
        EndToEndTestStepDefinitions.driverId = driverId;
        changeRideStatusRequest = new ChangeRideStatusRequest(RIDE_ID, driverId);
    }

    @When("A driver starts the ride")
    public void driverStartsTheRide() {
        rideResponse = rideClient.confirmDriverArrival(changeRideStatusRequest);
    }

    @Then("A ride's status should be changed to {string}")
    public void rideSStatusShouldBeChangedTo(String rideStatus) {
        assertThat(rideResponse.status())
                .isEqualTo(rideStatus);
    }

    @When("A passenger with id {long} apply promo-code with name {string}")
    public void passengerWithIdApplyPromoCodeWithName(long passengerId, String promoCodeName) {
        PromoCodeApplyRequest promoCodeApplyRequest = new PromoCodeApplyRequest(RIDE_ID, passengerId, promoCodeName);
        costBeforePromoCode = rideClient.getRideById(RIDE_ID)
                .cost();
        rideResponse = rideClient.applyPromoCode(promoCodeApplyRequest);
    }

    @Then("A ride's status of promoCodeApply is true")
    public void rideStatusOfPromoCodeApplyIsTrue() {
        assertThat(rideResponse.isPromoCodeApplied())
                .isTrue();
    }

    @And("Ride has lower price")
    public void rideHasLowerPrice() {
        assertThat(rideResponse.cost())
                .isLessThanOrEqualTo(costBeforePromoCode);
    }

    @When("A driver with id {long} finishes the ride")
    public void driverWithIdFinishesTheRide(long driverId) {
        EndToEndTestStepDefinitions.driverId = driverId;
        changeRideStatusRequest = new ChangeRideStatusRequest(RIDE_ID, driverId);
        rideResponse = rideClient.finishRide(changeRideStatusRequest);
    }

    @And("Finish date should be set")
    public void finishDateShouldBeSet() {
        assertThat(rideResponse.endTime())
                .isNotNull();
    }

    @And("A driver's status should be changed to {string}")
    public void driverSStatusShouldBeChangedTo(String driverStatus) {
        driverResponse = driverClient.getDriverById(driverId);
        assertThat(driverResponse.driverStatus())
                .isEqualTo(driverStatus);
    }
}