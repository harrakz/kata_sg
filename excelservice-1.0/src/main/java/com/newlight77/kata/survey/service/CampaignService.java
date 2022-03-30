package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.model.Campaign;

public interface CampaignService {
	void createCampaign(Campaign campaign);

	Campaign getCampaign(String id);
}
