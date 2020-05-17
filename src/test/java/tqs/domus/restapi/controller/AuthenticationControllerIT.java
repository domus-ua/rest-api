package tqs.domus.restapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.RestApiApplication;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;
import tqs.domus.restapi.service.LocadorService;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 07/mai/2020
 * @time 01:03
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestApiApplication.class)
@AutoConfigureMockMvc
public class AuthenticationControllerIT {

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador", "M");

	@Autowired
	private MockMvc servlet;

	@Autowired
	private UserRepository repository;

	@Autowired
	private LocadorService service;

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	private void createTestLocador() throws ErrorDetails {
		service.registerLocador(userDTO);
	}

	@Test
	void testLogin_ValidCredentials() throws Exception {
		createTestLocador();
		String credentials = userDTO.getEmail() + ":" + userDTO.getPassword();
		String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

		servlet.perform(post("/users/login")
				.header("Authorization", "Basic " + base64Credentials))
				.andExpect(status().isOk());
	}

	@Test
	void testLogin_InvalidCredentials() throws Exception {
		String credentials = "non_existent@email.com:pwd";
		String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

		servlet.perform(post("/users/login")
				.header("Authorization", "Basic " + base64Credentials))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testLogout() throws Exception {
		servlet.perform(get("/users/logout")).andExpect(status().isOk());
	}
}
