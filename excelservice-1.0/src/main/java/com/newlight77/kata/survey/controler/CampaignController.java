package com.newlight77.kata.survey.controler;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

	@Autowired
	private ExportCampaignService exportCampaignService;

	
	@PostMapping
	public ResponseEntity<Void> createCampaign(@RequestBody Campaign campaign) {
		if (Objects.isNull(campaign)) {
			return ResponseEntity.noContent().build();
		}
		exportCampaignService.createCampaign(campaign);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	

	@GetMapping(value = "{id}")
	public Campaign getCampaign(@PathVariable String id) {

		Campaign campaign = exportCampaignService.getCampaign(id);
		if (Objects.isNull(campaign)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Campaign with " + id + " is not found");
		}
		return campaign;

	}

	
	
	
	@PostMapping(value = "/export/{campaignId}")
	public ResponseEntity<Void> exportCampaign(@PathVariable String campaignId) {
		Campaign campaign = exportCampaignService.getCampaign(campaignId);
		if (Objects.isNull(campaign)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"The campaign with " + campaignId + " is not found");
		}
		Survey survey = exportCampaignService.getSurvey(campaign.getSurveyId());

		exportCampaignService.sendResults(campaign, survey);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
