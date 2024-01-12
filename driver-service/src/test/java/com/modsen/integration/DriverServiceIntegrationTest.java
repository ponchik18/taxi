package com.modsen.integration;

import com.modsen.constants.DriverServiceConstants;
import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverResponse;
import com.modsen.dto.driver.DriverStatusChangeRequest;
import com.modsen.enums.DriverStatus;
import com.modsen.exception.ErrorMessageResponse;
import com.modsen.model.Driver;
import com.modsen.repository.DriverRepository;
import com.modsen.util.DriverServiceClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:data/insert-test-data.sql")
public class DriverServiceIntegrationTest extends TestBaseContainer {

    @LocalServerPort
    private Integer port;
    @Autowired
    private DriverRepository driverRepository;

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = DriverServiceConstants.Path.DRIVER_CONTROLLER_PATH;
    }

    @Test
    public void getAllDrivers_ValidRequest_Success() {
        Response actualResponse = DriverServiceClient.getAllDrivers();

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("driverCount", greaterThan(0));
    }

    @Test
    public void getDriverById_ValidId_Success() {
        long driverId = 9L;

        Response actualResponse = DriverServiceClient.getDriver(driverId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("data/driver-response.json"))
                .body("id", equalTo(Long.valueOf(driverId).intValue()));
    }

    @Test
    public void createDriver_ValidRequest_Success() {
        DriverRequest driverRequest = new DriverRequest("lic_123456789", "Test", "Test", "test_request45@test.com", "+375111781178", DriverStatus.AVAILABLE.name());

        Response actualresponse = DriverServiceClient.postDriver(driverRequest);

        DriverResponse actualDriverResponse = actualresponse.then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("data/driver-response.json"))
                .extract()
                .as(DriverResponse.class);
        assertThat(actualDriverResponse)
                .usingRecursiveComparison()
                .ignoringFields("id", "driverStatus")
                .isEqualTo(driverRequest);
        Driver actualDriver = driverRepository.findById((long) actualDriverResponse.id())
                .orElseThrow();
        assertThat(actualDriver).usingRecursiveComparison()
                .ignoringFields("driverStatus")
                .isEqualTo(actualDriver);
    }

    @Test
    public void updateDriver_ValidIdAndRequest_Success() {
        long driverId = 6L;
        DriverRequest driverRequest = new DriverRequest("lic_12345677898", "Test", "Test", "test_request412@test.com", "+375181784178", DriverStatus.AVAILABLE.name());
        DriverResponse expectedDriver = DriverResponse.builder()
                .id(Long.valueOf(driverId).intValue())
                .licenseNumber(driverRequest.getLicenseNumber())
                .lastName(driverRequest.getLastName())
                .firstName(driverRequest.getFirstName())
                .phone(driverRequest.getPhone())
                .email(driverRequest.getEmail())
                .driverStatus(driverRequest.getDriverStatus())
                .build();

        Response actualResponse = DriverServiceClient.updateDriver(driverId, driverRequest);

        DriverResponse actualDriverResponse = actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("data/driver-response.json"))
                .extract()
                .body()
                .as(DriverResponse.class);
        assertThat(actualDriverResponse)
                .isEqualTo(expectedDriver);
        Driver actualDriver = driverRepository.findById(driverId)
                .orElseThrow();
        assertThat(actualDriver).usingRecursiveComparison()
                .ignoringFields("id", "driverStatus")
                .isEqualTo(expectedDriver);
    }

    @Test
    public void updateDriver_InValidRequest_BadRequest() {
        long driverId = 1L;
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        Response actualResponse = DriverServiceClient.updateDriver(driverId, new DriverRequest());

        actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .body("statusCode", equalTo(expectedStatus));
    }

    @Test
    public void createDriver_DuplicateEmail_ExceptionThrown() {
        String email = "john.doe@example.com";
        DriverRequest driverRequest = new DriverRequest("lic_123456789", "Test", "Test", "test_request45@test.com", "+375111781178", DriverStatus.AVAILABLE.name());
        driverRequest.setEmail(email);
        int expectedStatus = HttpStatus.CONFLICT.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_EMAIL, email))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = DriverServiceClient.postDriver(driverRequest);

        ErrorMessageResponse actualMessage = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualMessage)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedMessage);
    }

    @Test
    public void createDriver_DuplicatePhone_ExceptionThrown() {
        String phone = "+375111111119";
        DriverRequest driverRequest = new DriverRequest("lic_123456789", "Test", "Test", "test_request45@test.com", "+375111781178", DriverStatus.AVAILABLE.name());
        driverRequest.setPhone(phone);

        int expectedStatus = HttpStatus.CONFLICT.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(DriverServiceConstants.Errors.Message.DUPLICATE_DRIVER_WITH_PHONE, phone))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = DriverServiceClient.postDriver(driverRequest);

        ErrorMessageResponse actualMessage = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualMessage)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedMessage);
    }

    @Test
    public void deleteDriver_ExistingId_Success() {
        long driverId = 5L;

        Response actualResponse = DriverServiceClient.deleteDriver(driverId);

        actualResponse.then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
        assertThat(driverRepository.existsById(driverId))
                .isFalse();
    }

    @Test
    public void deleteDriver_NonExistingId_ExceptionThrown() {
        long driverId = 999L;
        int expectedStatus = HttpStatus.NOT_FOUND.value();
        ErrorMessageResponse expectedMessage = ErrorMessageResponse.builder()
                .message(String.format(DriverServiceConstants.Errors.Message.DRIVER_NOT_FOUND, driverId))
                .statusCode(expectedStatus)
                .build();

        Response actualResponse = DriverServiceClient.deleteDriver(driverId);

        ErrorMessageResponse actualMessage = actualResponse.then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ErrorMessageResponse.class);
        assertThat(actualMessage)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedMessage);
    }

    @Test
    public void changeStatus_ValidDriverStatusChangeRequest_Success() {
        long driverId = 6L;
        DriverStatusChangeRequest driverStatusChangeRequest = new DriverStatusChangeRequest(driverId, DriverStatus.OFFLINE.name());

        DriverServiceClient.patchDriver(driverStatusChangeRequest);

        Driver actualDriver = driverRepository.findById(driverId)
                .orElseThrow();
        assertThat(actualDriver.getDriverStatus().name())
                .isEqualTo(driverStatusChangeRequest.getStatus());
    }
}