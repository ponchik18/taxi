package com.modsen.util;

import com.modsen.dto.driver.DriverRequest;
import com.modsen.dto.driver.DriverStatusChangeRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DriverServiceClient {

    public static Response getAllDrivers() {
        return given()
                .when()
                .get()
                .then()
                .extract()
                .response();
    }

    public static Response getDriver(long driverId) {
        return given()
                .when()
                .get("/" + driverId)
                .then()
                .extract()
                .response();
    }


    public static Response deleteDriver(long driverId) {
        return given()
                .when()
                .delete("/" + driverId)
                .then()
                .extract()
                .response();
    }

    public static Response postDriver(DriverRequest driverRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(driverRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();
    }

    public static Response updateDriver(long driverId, DriverRequest driverRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(driverRequest)
                .when()
                .put("/" + driverId)
                .then()
                .extract()
                .response();
    }

    public static Response patchDriver(DriverStatusChangeRequest driverStatusChangeRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(driverStatusChangeRequest)
                .when()
                .patch("/status")
                .then()
                .extract()
                .response();
    }
}