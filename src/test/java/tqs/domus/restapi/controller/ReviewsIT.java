package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tqs.domus.restapi.RestApiApplication;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.HouseReviewDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;
import tqs.domus.restapi.service.HouseService;
import tqs.domus.restapi.service.LocadorService;
import tqs.domus.restapi.service.LocatarioService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 23/mai/2020
 * @time 17:37
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestApiApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ReviewsIT {
	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private MockMvc servlet;

	@Autowired
	private UserRepository repository;

	@Autowired
	private LocadorService locadorService;

	@Autowired
	private LocatarioService locatarioService;

	@Autowired
	private HouseService houseService;

	private Locador locador;

	private Locatario locatario1, locatario2;

	private House house;

	@BeforeEach
	public void initDb() throws ErrorDetails, ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		locador = locadorService.registerLocador(userDTO);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());
		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
		house = houseService.registerHouse(houseDTO);

		userDTO = new UserDTO("v1@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		locatario1 = locatarioService.registerLocatario(userDTO);

		userDTO = new UserDTO("v2@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		locatario2 = locatarioService.registerLocatario(userDTO);

		HouseReviewDTO reviewDTO = new HouseReviewDTO(house.getId(), locatario1.getId(), "Review", 3.0);
		houseService.registerReview(reviewDTO);

		HouseReviewDTO reviewDTO2 = new HouseReviewDTO(house.getId(), locatario2.getId(), "Review", 3.0);
		houseService.registerReview(reviewDTO2);
	}

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	void testCreateHouseReview_houseDoesNotExist() throws Exception {
		HouseReviewDTO reviewDTO2 = new HouseReviewDTO(0L, locatario2.getId(), "Review", 3.0);

		String houseJsonString = mapper.writeValueAsString(reviewDTO2);

		servlet.perform(post("/houses/reviews")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateHouseReview_locatarioDoesNotExist() throws Exception {
		HouseReviewDTO reviewDTO2 = new HouseReviewDTO(house.getId(), 0L, "Review", 3.0);

		String houseJsonString = mapper.writeValueAsString(reviewDTO2);

		servlet.perform(post("/houses/reviews")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test()
	void testDeleteHouseReview_reviewDoesNotExist() throws Exception {
		servlet.perform(delete("/houses/reviews/" + (house.getId() - 1) + "/" + (locatario1.getId() - 1))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteHouseReview_reviewExists() throws Exception {
		servlet.perform(delete("/houses/reviews/" + house.getId() + "/" + locatario1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
}
