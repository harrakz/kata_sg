package com.newlight77.kata.survey.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String id;
    private String streetNumber;
    private String streetName;
    private String postalCode;
    private String city;
}
