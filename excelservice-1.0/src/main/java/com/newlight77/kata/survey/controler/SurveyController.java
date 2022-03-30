package com.newlight77.kata.survey.controler;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.SurveyService;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
	@Autowired
	private SurveyService surveyService;

	private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);

	@PostMapping
	public ResponseEntity<Void> createSurvey(@RequestBody Survey survey) {
		if (Objects.isNull(survey)) {
			return ResponseEntity.noContent().build();
		}
		try {
			surveyService.creerSurvey(survey);
		} catch (Exception e) {
			throw e;

		}
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	

	@GetMapping(value = "{id}")
	public Survey getSurvey(@PathVariable String id) {
		Survey survey = surveyService.getSurvey(id);
		if (Objects.isNull(survey)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The survey with " + id + " is not found");
		}
		return survey;
	}
}
