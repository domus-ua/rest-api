package tqs.domus.restapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.RestApiApplication;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;
import tqs.domus.restapi.service.HouseService;
import tqs.domus.restapi.service.LocadorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 18/mai/2020
 * @time 11:00
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestApiApplication.class)
@AutoConfigureMockMvc
public class CitiesControllerIT {

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador", "M");

	@Autowired
	private MockMvc servlet;

	@Autowired
	private UserRepository repository;

	@Autowired
	private LocadorService locadorService;

	@Autowired
	private HouseService houseService;

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	void testGetAllCities_isEmpty() throws Exception {
		servlet.perform(get("/cities")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(Collections.emptyList())));
	}

	@Test
	void testGetAllCities_isNotEmpty() throws Exception {
		Locador locador = locadorService.registerLocador(userDTO);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "Aveiro", "3700-191", 2, 2, 2, 300, true,
				230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
		HouseDTO houseDTO2 = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2
				, 300, true, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);

		houseService.registerHouse(houseDTO);
		houseService.registerHouse(houseDTO2);
		List<String> cities = new ArrayList<>();
		cities.add(houseDTO.getCity());
		cities.add(houseDTO2.getCity());

		servlet.perform(get("/cities")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(equalTo(cities))));
	}
}
