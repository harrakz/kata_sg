package com.newlight77.kata.survey.service.impl;

import java.util.Optional;
import java.util.UUID;

import com.newlight77.kata.survey.client.impl.CampaignClientImpl;
import com.newlight77.kata.survey.exception.CreatedException;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.service.CampaignService;

public class CampaignServiceImpl implements CampaignService {

	private CampaignClientImpl campaignWebService;
	@Override
	public void createCampaign(Campaign campaign) {

		Optional.ofNullable(campaign).orElseThrow(() -> new CreatedException("Campaign object is null "));
		campaign.setId(UUID.randomUUID().toString());
		
		campaignWebService.createCampaign(campaign);

	}

	@Override
	public Campaign getCampaign(String id) {

		return campaignWebService.getCampaign(id);
	}

}
