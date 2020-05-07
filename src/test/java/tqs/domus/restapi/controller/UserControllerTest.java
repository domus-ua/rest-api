package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 23:29
 */

@WebMvcTest(value = UserController.class)
public class UserControllerTest {

	private final ObjectMapper mapper = new ObjectMapper();

	private UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");

	@Autowired
	private MockMvc servlet;

	@MockBean
	private UserService service;

	@Test
	void testCreateUser_EmailIsFree() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.registerUser(any(UserDTO.class))).willReturn(user);

		servlet.perform(post("/users")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("email", is(user.getEmail())))
				.andExpect(jsonPath("firstName", is(user.getFirstName())))
				.andExpect(jsonPath("lastName", is(user.getLastName())))
				.andExpect(jsonPath("phoneNumber", is(user.getPhoneNumber())))
				.andExpect(jsonPath("role", is(user.getRole())))
				.andExpect(jsonPath("dateJoined", is(user.getDateJoined())))
				.andExpect(jsonPath("lastLogin", is(user.getLastLogin())));

		reset(service);
	}
}
