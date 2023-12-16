package com.modsen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.constants.PassengerServiceConstants;
import com.modsen.dto.passenger.PassengerListResponse;
import com.modsen.dto.passenger.PassengerRequest;
import com.modsen.dto.passenger.PassengerResponse;
import com.modsen.service.PassengerService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PassengerController.class)
@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @MockBean
    private PassengerService passengerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private PassengerResponse passengerResponse;
    private PassengerRequest passengerRequest;

    @BeforeEach
    public void init() {
        passengerResponse = PassengerResponse.builder()
                .email("test@test.com")
                .phone("+375111111111")
                .firstName("Test")
                .lastName("Test")
                .id(1)
                .build();
        passengerRequest = new PassengerRequest("Test", "Test", "test@test.com", "+375111111111");
    }

    @Test
    void testGetAllPassenger() throws Exception {
        PassengerListResponse passengerListResponse = PassengerListResponse.builder()
                .passengers(List.of(passengerResponse))
                .passengerCount(1)
                .build();

        when(passengerService.getAllPassenger(any())).thenReturn(passengerListResponse);
        ResultActions response = mockMvc.perform(get(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passengerCount", CoreMatchers.is(passengerListResponse.passengerCount())));
    }

    @Test
    void testSuccessfullyCreatePassenger() throws Exception {
        given(passengerService.createPassenger((any())))
                .willAnswer(invocation -> passengerResponse);

        ResultActions response = mockMvc.perform(post(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(passengerResponse.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(passengerResponse.firstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(passengerResponse.lastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(passengerResponse.email())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.is(passengerResponse.phone())));

    }

    @Test
    void tetGetPassengerById() throws Exception {
        int passengerId = passengerResponse.id();

        when(passengerService.getPassengerById(passengerId)).thenReturn(passengerResponse);
        ResultActions response = mockMvc.perform(get(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(passengerId)));
    }

    @Test
    void testSuccessfullyUpdatePassenger() throws Exception {
        int passengerId = passengerResponse.id();
        given(passengerService.updatePassenger(anyLong(), any()))
                .willAnswer(invocation -> passengerResponse);

        ResultActions response = mockMvc.perform(put(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(passengerResponse.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(passengerResponse.firstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(passengerResponse.lastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(passengerResponse.email())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.is(passengerResponse.phone())));
    }

    @Test
    void deletePassenger() throws Exception {
        int passengerId = passengerResponse.id();

        when(passengerService.getPassengerById(passengerId)).thenReturn(passengerResponse);
        ResultActions response = mockMvc.perform(delete(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH + "/" + passengerId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testValidationFailsForEmptyFields() throws Exception {
        PassengerRequest passengerRequest = new PassengerRequest("", "", "", "");

        ResultActions response = mockMvc.perform(post(PassengerServiceConstants.Path.PASSENGER_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerRequest)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}