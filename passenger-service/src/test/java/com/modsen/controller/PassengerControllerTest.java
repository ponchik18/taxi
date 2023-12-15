package com.modsen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.service.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PassengerController.class)
class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPassenger() {
    }

    @Test
    void createPassenger() {
    }

    @Test
    void getPassengerById() {
    }

    @Test
    void updatePassenger() {
    }

    @Test
    void deletePassenger() {
    }
}