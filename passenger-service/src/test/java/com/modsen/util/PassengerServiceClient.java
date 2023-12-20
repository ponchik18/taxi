package com.modsen.util;

import com.modsen.dto.passenger.PassengerRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PassengerServiceClient {

    public static Response getAllPassengers() {
        return given()
                .when()
                .get()
                .then()
                .extract()
                .response();
    }

    public static Response getPassenger(long passengerId) {
        return given()
                .when()
                .get("/" + passengerId)
                .then()
                .extract()
                .response();
    }


    public static Response deletePassenger(long passengerId) {
        return given()
                .when()
                .delete("/" + passengerId)
                .then()
                .extract()
                .response();
    }

    public static Response postPassenger(PassengerRequest passengerRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();
    }

    public static Response updatePassenger(long passengerId, PassengerRequest passengerRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .put("/" + passengerId)
                .then()
                .extract()
                .response();
    }
}