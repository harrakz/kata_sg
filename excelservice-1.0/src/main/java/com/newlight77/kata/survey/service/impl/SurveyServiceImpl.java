package com.newlight77.kata.survey.service.impl;

import java.util.Optional;
import java.util.UUID;

import com.newlight77.kata.survey.client.impl.CampaignClientImpl;
import com.newlight77.kata.survey.exception.CreatedException;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.SurveyService;

public class SurveyServiceImpl implements SurveyService {
	public CampaignClientImpl campaignWebService;
	public MailServiceImpl mailService;

	public SurveyServiceImpl() {
	}

	@Override
	public void creerSurvey(Survey survey) {
		
		Optional.ofNullable(survey).orElseThrow(() -> new CreatedException("Survey object is null "));
		survey.setId(UUID.randomUUID().toString());
		campaignWebService.createSurvey(survey);
	}

	@Override
	public Survey getSurvey(String id) {
		return campaignWebService.getSurvey(id);
	}

}