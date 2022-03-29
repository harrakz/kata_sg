package com.newlight77.kata.survey.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.validation.constraints.Negative;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Survey {
    private String id;
    private String sommary;
    private String client;
    private Address clientAddress;
    private List<Question> questions;
}

