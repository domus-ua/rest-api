package tqs.domus.restapi.controller;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.UserService;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 07/mai/2020
 * @time 01:03
 */

@WebMvcTest(value = AuthenticationController.class)
public class AuthenticationControllerTest {

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador", "M");

	@Autowired
	private MockMvc servlet;

	@MockBean
	private UserService service;

	@Test
	void testLogin_ValidCredentials() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		String credentials = user.getEmail() + ":" + user.getPassword();
		String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

		given(service.login(anyString(), anyString())).willReturn(user);

		servlet.perform(post("/users/login")
				.header("Authorization", "Basic " + base64Credentials))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testLogin_InvalidCredentials() throws Exception {
		String credentials = "non_existent@email.com:pwd";
		String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

		given(service.login(anyString(), anyString())).willThrow(new ErrorDetails("Error"));

		servlet.perform(post("/users/login")
				.header("Authorization", "Basic " + base64Credentials))
				.andExpect(status().isBadRequest());

		reset(service);
	}

	@Test
	void testLogout() throws Exception {
		servlet.perform(get("/users/logout")).andExpect(status().isOk());
	}
}
