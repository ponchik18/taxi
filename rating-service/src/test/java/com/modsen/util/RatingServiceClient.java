package com.modsen.util;

import com.modsen.dto.rating.RatingRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RatingServiceClient {

    public static Response getAllRating() {
        return given()
                .when()
                .get()
                .then()
                .extract()
                .response();
    }

    public static Response getRating(long ratingId) {
        return given()
                .when()
                .get("/" + ratingId)
                .then()
                .extract()
                .response();
    }


    public static Response deleteRating(long ratingId) {
        return given()
                .when()
                .delete("/" + ratingId)
                .then()
                .extract()
                .response();
    }

    public static Response postRating(RatingRequest ratingRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(ratingRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();
    }
}