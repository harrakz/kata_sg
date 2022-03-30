package com.newlight77.kata.survey.service;

import org.apache.poi.ss.usermodel.Workbook;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;

public interface ExportCampaignService {

	void sendResults(Campaign campaign, Survey survey);
	
	void writeFileAndSend(Survey survey, Workbook workbook) ;



}
