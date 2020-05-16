package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.LocatarioService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 11:58
 */

@WebMvcTest(value = LocatarioController.class)
public class LocatarioControllerTest {

	private final ObjectMapper mapper = new ObjectMapper();

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

	@Autowired
	private MockMvc servlet;

	@MockBean
	private LocatarioService service;

	@Test
	void testCreateLocatario_EmailIsFree() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.registerLocatario(any(UserDTO.class))).willReturn(locatario);

		servlet.perform(post("/locatarios")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(user.getEmail())))
				.andExpect(jsonPath("user.firstName", is(user.getFirstName())))
				.andExpect(jsonPath("user.lastName", is(user.getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(user.getPhoneNumber())))
				.andExpect(jsonPath("user.dateJoined", is(user.getDateJoined())))
				.andExpect(jsonPath("user.lastLogin", is(user.getLastLogin())))
				.andExpect(jsonPath("user.sex", is(user.getSex())))
				.andExpect(jsonPath("user.photo", is(user.getPhoto())))
				.andExpect(jsonPath("role", is(locatario.getRole())))
				.andExpect(jsonPath("reviews", is(locatario.getReviews())))
				.andExpect(jsonPath("reviewsReceived", is(locatario.getReviewsReceived())));

		reset(service);
	}

	@Test
	void testCreateLocatario_EmailIsTaken() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.registerLocatario(any(UserDTO.class))).willThrow(new ErrorDetails("Error"));

		servlet.perform(post("/locatarios")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		reset(service);
	}

	@Test
	void testGetLocatarioById_existentId() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		given(service.getLocatarioById(anyLong())).willReturn(locatario);

		servlet.perform(get("/locatarios/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(user.getEmail())))
				.andExpect(jsonPath("user.firstName", is(user.getFirstName())))
				.andExpect(jsonPath("user.lastName", is(user.getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(user.getPhoneNumber())))
				.andExpect(jsonPath("user.dateJoined", is(user.getDateJoined())))
				.andExpect(jsonPath("user.lastLogin", is(user.getLastLogin())))
				.andExpect(jsonPath("user.sex", is(user.getSex())))
				.andExpect(jsonPath("user.photo", is(user.getPhoto())))
				.andExpect(jsonPath("role", is(locatario.getRole())))
				.andExpect(jsonPath("reviews", is(locatario.getReviews())))
				.andExpect(jsonPath("reviewsReceived", is(locatario.getReviewsReceived())));

		reset(service);
	}

	@Test
	void testGetLocatarioById_inexistentId() throws Exception {
		given(service.getLocatarioById(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/locatarios/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}
}
