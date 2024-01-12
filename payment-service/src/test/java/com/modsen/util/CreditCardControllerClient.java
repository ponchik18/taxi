package com.modsen.util;

import com.modsen.dto.card.CreditCardRequest;
import com.modsen.dto.card.DefaultCreditCardRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreditCardControllerClient {

    public static Response getAllUserCreditCard(long userId, String userRole) {
        return given()
                .queryParam("userId", userId)
                .queryParam("userRole", userRole)
                .when()
                .get()
                .then()
                .extract()
                .response();
    }

    public static Response getCreditCard(long creditCard) {
        return given()
                .when()
                .get("/" + creditCard)
                .then()
                .extract()
                .response();
    }


    public static Response deleteCreditCard(long cardId, long userId, String userRole) {
        return given()
                .queryParam("userId", userId)
                .queryParam("userRole", userRole)
                .when()
                .delete("/" + cardId)
                .then()
                .extract()
                .response();
    }

    public static Response postCreditCard(CreditCardRequest creditCardRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(creditCardRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();
    }

    public static Response makeCreditCardDefault(DefaultCreditCardRequest defaultCreditCardRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(defaultCreditCardRequest)
                .when()
                .put("/default")
                .then()
                .extract()
                .response();
    }

    public static Response getDefaultCardForUser(long userId, String userRole) {
        return given()
                .queryParam("userId", userId)
                .queryParam("userRole", userRole)
                .when()
                .get("/default")
                .then()
                .extract()
                .response();
    }
}