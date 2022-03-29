package com.newlight77.kata.survey.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {
    private String id ;
    private String surveyId;
    private List<AddressStatus> addressStatuses;
}
