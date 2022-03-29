package com.newlight77.kata.survey.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlight77.kata.survey.controler.CampaignController;
import com.newlight77.kata.survey.model.Address;
import com.newlight77.kata.survey.model.AddressStatus;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Question;
import com.newlight77.kata.survey.model.Status;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.impl.ExportCampaignServiceImpl;

public class CampaignControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private CampaignController campaignController;

	@Mock
	private ExportCampaignServiceImpl exportCampaignService;

	private static ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(campaignController).build();
	}

	@Test
	public void get_campaign() throws Exception {

		Address clientAddress1 = Address.builder().city("Colombes").postalCode("92001").streetName("AVENUE DE BEZON")
				.streetNumber("98").build();

		AddressStatus addressStatus = AddressStatus.builder().id("11").status(Status.TODO).address(clientAddress1)
				.build();

		List<AddressStatus> addressStatuses = new ArrayList<>();
		addressStatuses.add(addressStatus);
		Campaign campaign = Campaign.builder().id("564").surveyId("5897").addressStatuses(addressStatuses).build();
		Mockito.when(exportCampaignService.getCampaign("564")).thenReturn(campaign);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/campaigns/564").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.id", is("564"))).andExpect(jsonPath("$.surveyId", is("5897")));

	}

	@Test
	public void create_campaign_sucess() throws Exception {

		Address clientAddress1 = Address.builder().city("Colombes").postalCode("92001").streetName("AVENUE DE BEZON")
				.streetNumber("98").build();

		AddressStatus addressStatus = AddressStatus.builder().id("11").status(Status.TODO).address(clientAddress1)
				.build();

		List<AddressStatus> addressStatuses = new ArrayList<>();
		addressStatuses.add(addressStatus);
		Campaign campaign = Campaign.builder().id("564").surveyId("5897").addressStatuses(addressStatuses).build();
		String json = mapper.writeValueAsString(campaign);

		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post("/api/campaigns").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	
	
	
	
	@Test
	public void create_export_sucess() throws Exception {

		Address clientAddress1 = Address.builder().city("poissy").postalCode("78500").streetName("AVENUE DE BEZON")
				.streetNumber("231").build();

		AddressStatus addressStatus = AddressStatus.builder().id("897").status(Status.TODO).address(clientAddress1)
				.build();

		List<AddressStatus> addressStatuses = new ArrayList<>();
		addressStatuses.add(addressStatus);
		Campaign campaign = Campaign.builder().id("897").surveyId("888").addressStatuses(addressStatuses).build();
		
		
		
		Address address = Address.builder().streetNumber("10").streetName("Lattre de tassigny").city("Nice")
				.postalCode("92001").build();
		Question question1 = Question.builder().id("12").question("Question one").build();
		Question question2 = Question.builder().id("13").question("Question tow").build();
		List<Question> questions = new ArrayList<Question>();
		questions.add(question1);
		questions.add(question2);
		Survey survey = Survey.builder().sommary("Sommary test expor").client("Client one").id("888")
				.clientAddress(address).questions(questions).build();
		
		Mockito.when(exportCampaignService.getCampaign("897")).thenReturn(campaign);
		Mockito.when(exportCampaignService.getSurvey("888")).thenReturn(survey);
		doNothing().when(exportCampaignService).sendResults(campaign, survey);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/campaigns/export/897").contentType(MediaType.APPLICATION_JSON)).andReturn();
	
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

}
