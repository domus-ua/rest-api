package tqs.domus.restapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.service.HouseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 18/mai/2020
 * @time 10:49
 */

@WebMvcTest(value = CitiesController.class)
public class CitiesControllerTest {

	@Autowired
	private MockMvc servlet;

	@MockBean
	private HouseService service;

	@Test
	void testGetAllCities_isEmpty() throws Exception {
		given(service.getAllCities()).willReturn(Collections.emptyList());

		servlet.perform(get("/cities")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(Collections.emptyList())));

		reset(service);
	}

	@Test
	void testGetAllCities_isNotEmpty() throws Exception {
		List<String> cities = new ArrayList<>();
		cities.add("Aveiro");
		cities.add("Viseu");

		given(service.getAllCities()).willReturn(cities);

		servlet.perform(get("/cities")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(equalTo(cities))));

		reset(service);
	}
}
