package com.newlight77.kata.survey.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlight77.kata.survey.controler.SurveyController;
import com.newlight77.kata.survey.model.Address;
import com.newlight77.kata.survey.model.Question;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;

@RunWith(MockitoJUnitRunner.class)
public class SurveyControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private SurveyController surveyController;

	@Mock
	private ExportCampaignService exportCampaignService;

	private static ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(surveyController).build();
	}

	@Test
	public void getSurveyById() throws Exception {

		Address clientAddress = new Address();
		clientAddress.setCity("Paris");
		clientAddress.setPostalCode("75001");
		clientAddress.setStreetName("Avenue Lattre de Tassigny");
		clientAddress.setStreetNumber("89");

		Question question1 = new Question();
		question1.setQuestion("Question 1");
		Question question2 = new Question();
		question1.setQuestion("Question 2");
		List<Question> questions = new ArrayList<>();
		questions.add(question1);
		questions.add(question2);
		Survey record_one = Survey.builder().sommary("Sommary test").client("Client 1").id("7998")
				.clientAddress(clientAddress).questions(questions).build();

		Mockito.when(exportCampaignService.getSurvey("7998")).thenReturn(record_one);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/surveys/7998").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.sommary", is("Sommary test"))).andExpect(jsonPath("$.client", is("Client 1")));

	}

	@Test
	public void create_survey_sucess() throws Exception {
		
		Address address = Address.builder().streetNumber("10").streetName("Lattre de tassigny").city("Nice")
				.postalCode("92001").build();
		Question question1 = Question.builder().id("12").question("Question one").build();
		Question question2 = Question.builder().id("13").question("Question tow").build();
		List<Question> questions = new ArrayList<Question>();
		questions.add(question1);
		questions.add(question2);
		Survey record_created = Survey.builder().sommary("Sommary one").client("Client one").id("5664")
				.clientAddress(address).questions(questions).build();
		String json = mapper.writeValueAsString(record_created);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/surveys")
			      .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		   assertEquals(200, status);

	}

}
