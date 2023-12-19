package com.modsen.util;

import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class PassengerServiceClient {

    public static void getAllPassengers() {
        given()
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("passengerCount", greaterThan(0));
    }

    public static void getPassenger(long passengerId) {
        given()
                .when()
                .get("/" + passengerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("data/passenger-response.json"))
                .body("id", equalTo(Long.valueOf(passengerId).intValue()));
    }


    public static void deletePassenger(long passengerId) {
        given()
                .when()
                .delete("/" + passengerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void deletePassengerNonExists(long passengerId) {
        given()
                .when()
                .delete("/" + passengerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    public static void postPassenger(PassengerRequest passengerRequest) {
        given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("data/passenger-response.json"))
                .body("firstName", equalTo(passengerRequest.getFirstName()))
                .body("lastName", equalTo(passengerRequest.getLastName()))
                .body("email", equalTo(passengerRequest.getEmail()))
                .body("phone", equalTo(passengerRequest.getPhone()));
    }

    public static void postPassengerWithDuplicate(PassengerRequest passengerRequest) {
        given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    public static PassengerResponse updatePassenger(long passengerId, PassengerRequest passengerRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .put("/" + passengerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("data/passenger-response.json"))
                .extract()
                .body()
                .as(PassengerResponse.class);
    }

    public static void updatePassengerInvalidRequest(long passengerId, PassengerRequest passengerRequest) {
        given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .put("/" + passengerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}