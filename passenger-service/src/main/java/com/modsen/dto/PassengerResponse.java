package com.modsen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassengerResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
}