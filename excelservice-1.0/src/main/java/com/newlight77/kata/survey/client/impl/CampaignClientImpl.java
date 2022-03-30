package com.newlight77.kata.survey.client.impl;

import com.newlight77.kata.survey.client.CampaignClient;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CampaignClientImpl  implements CampaignClient{

	@Autowired
    private WebClient webClient;

    public CampaignClientImpl(@Value("${external.url}") String externalUrl) {
        webClient = WebClient.builder()
                .baseUrl(externalUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();;
    }

    @Override
    public void createSurvey(Survey survey) {
        webClient.post()
                .uri("/surveys")
                .syncBody(survey)
                .retrieve();
    }

    @Override
    public Survey getSurvey(String id) {
        return webClient.get()
                .uri("/surveys/" + id)
                .retrieve()
                .bodyToMono(Survey.class).block();
    }

    @Override
    public void createCampaign(Campaign campaign) {
        webClient.post()
                .uri("/campaigns")
                .syncBody(campaign);
    }

    @Override
    public Campaign getCampaign(String id) {
        return webClient.get()
                .uri("/campaigns/" + id)
                .retrieve()
                .bodyToMono(Campaign.class).block();
    }
}
