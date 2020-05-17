package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.RestApiApplication;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;
import tqs.domus.restapi.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 23:29
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestApiApplication.class)
@AutoConfigureMockMvc
public class UserControllerIT {

	private final ObjectMapper mapper = new ObjectMapper();

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador", "M");

	@Autowired
	private MockMvc servlet;

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserService service;

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	void testCreateUser_EmailIsFree() throws Exception {
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(post("/users")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("email", is(userDTO.getEmail())))
				.andExpect(jsonPath("firstName", is(userDTO.getFirstName())))
				.andExpect(jsonPath("lastName", is(userDTO.getLastName())))
				.andExpect(jsonPath("phoneNumber", is(userDTO.getPhoneNumber())))
				.andExpect(jsonPath("sex", is(userDTO.getSex())))
				.andExpect(jsonPath("photo", is(userDTO.getPhoto())));
	}

	@Test
	void testCreateUser_EmailIsTaken() throws Exception {
		service.registerUser(userDTO);
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(post("/users")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
