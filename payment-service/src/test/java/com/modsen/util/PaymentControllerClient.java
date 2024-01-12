package com.modsen.util;

import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PayoutRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PaymentControllerClient {

    public static Response getPaymentHistory() {
        return given()

                .when()
                .get("/history")
                .then()
                .extract()
                .response();
    }

    public static Response postCharge(PaymentRequest paymentRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(paymentRequest)
                .when()
                .post("/charge")
                .then()
                .extract()
                .response();
    }
}