package com.modsen.constant;

public interface EndToEndTestConstant {

    interface TestData {

    }

    interface ServiceName {
        String DRIVER_SERVICE_NAME = "driver-service";
        String PASSENGER_SERVICE_NAME = "passenger-service";
        String RIDES_SERVICE_NAME = "rides-service";
        String RATING_SERVICE_NAME = "rating-service";
    }

    interface ServiceBasePath {
        String DRIVER_SERVICE_PATH = "/api/v1/driver";
        String PASSENGER_SERVICE_PATH = "/api/v1/passenger";
        String RIDES_SERVICE_PATH = "/api/v1/rides";
        String RATING_SERVICE_PATH = "/api/v1/rating";
    }
}