package tech.corydaniel.hibernatepoc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import tech.corydaniel.hibernatepoc.model.Person;
import tech.corydaniel.hibernatepoc.model.PersonInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HPControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		this.objectMapper = new ObjectMapper();
	}
	
	@After
	public void tearDown() {
		this.mockMvc = null;
		this.objectMapper = null;
	}
	
	@Test
	public void testPersonNameSuccess() throws Exception {
		this.mockMvc.perform(get("/person/1/name"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().string("Jimmy"));
	}
	
	@Test
	public void testGET_badURL_Invalid() throws Exception {
		this.mockMvc.perform(get("/badURL"))
			.andExpect(status().isNotFound());	
	}
	
	@Test
	public void testPerson_PersonNotFound() throws Exception {
		this.mockMvc.perform(get("/person/2/name"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testCreatePerson_Success() throws Exception {
		String contentStr = this.objectMapper.writeValueAsString(
				new PersonInfo(new Person("cory", aYearAgo())));		
		this.mockMvc.perform(post("/person").content(contentStr).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	public void testCreatePerson_MissingBody_Invalid() throws Exception {
		this.mockMvc.perform(post("/person").content("").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreatePerson_EmptyPersonInfo_Invalid() throws Exception {
		this.mockMvc.perform(post("/person")
				.content(this.objectMapper.writeValueAsString(
						new PersonInfo()))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());				
	}
	
	@Test
	public void testCreatePerson_ExtraField() throws Exception {
		String content = "{\"person\":{\"name\":\"cory\",\"dob\":\"2018-03-11T19:46:22.759\",\"extrafield\":\"something\"}}";
		this.mockMvc.perform(post("/person")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testCreatePerson_MalformedDateField_Invalid() throws Exception {
		String contentStr = "{\"person\":{\"name\":\"cory\",\"dob\":\"WhatIsADob\"}}";
		this.mockMvc.perform(post("/person").content(contentStr).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreatePerson_DateInFuture_Invalid() throws Exception {
		String contentStr = this.objectMapper.writeValueAsString(
				new PersonInfo(new Person("cory", nextYear())));		
		this.mockMvc.perform(post("/person").content(contentStr).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	private static Date aYearAgo() {
		Calendar aYearAgo = Calendar.getInstance();
		aYearAgo.add(Calendar.YEAR, -1);
		return aYearAgo.getTime();
	}
	
	private static Date nextYear() {
		Calendar aYearAgo = Calendar.getInstance();
		aYearAgo.add(Calendar.YEAR, 1);
		return aYearAgo.getTime();
	}

}
