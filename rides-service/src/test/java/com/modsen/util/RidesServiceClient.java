package com.modsen.util;

import com.modsen.dto.promo.PromoCodeApplyRequest;
import com.modsen.dto.rides.ChangeRideStatusRequest;
import com.modsen.dto.rides.RideDriverRequest;
import com.modsen.dto.rides.RidePassengerRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RidesServiceClient {

    public static Response getAllRide() {
        return given()
                .when()
                .get()
                .then()
                .extract()
                .response();
    }

    public static Response getRide(long rideId) {
        return given()
                .when()
                .get("/" + rideId)
                .then()
                .extract()
                .response();
    }


    public static Response deleteRide(long rideId) {
        return given()
                .when()
                .delete("/" + rideId)
                .then()
                .extract()
                .response();
    }

    public static Response postRide(RidePassengerRequest ridePassengerRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(ridePassengerRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();
    }

    public static Response updateRide(long rideId, RideDriverRequest rideDriverRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(rideDriverRequest)
                .when()
                .put("/" + rideId)
                .then()
                .extract()
                .response();
    }

    public static Response cancelRide(ChangeRideStatusRequest changeRideStatusRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(changeRideStatusRequest)
                .when()
                .put("/cancel")
                .then()
                .extract()
                .response();
    }

    public static Response applyPromoCode(PromoCodeApplyRequest promoCodeApplyRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(promoCodeApplyRequest)
                .when()
                .put("/promo-code")
                .then()
                .extract()
                .response();
    }
}