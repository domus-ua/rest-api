package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.RestApiApplication;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;
import tqs.domus.restapi.service.LocatarioService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 11:58
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestApiApplication.class)
@AutoConfigureMockMvc
public class LocatarioControllerIT {

	private final ObjectMapper mapper = new ObjectMapper();

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

	@Autowired
	private MockMvc servlet;

	@Autowired
	private UserRepository repository;

	@Autowired
	private LocatarioService service;

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	void testCreateLocatario_EmailIsFree() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(post("/locatarios")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(user.getEmail())))
				.andExpect(jsonPath("user.firstName", is(user.getFirstName())))
				.andExpect(jsonPath("user.lastName", is(user.getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(user.getPhoneNumber())))
				.andExpect(jsonPath("user.lastLogin", is(user.getLastLogin())))
				.andExpect(jsonPath("user.sex", is(user.getSex())))
				.andExpect(jsonPath("user.photo", is(user.getPhoto())))
				.andExpect(jsonPath("role", is(locatario.getRole())));
	}

	@Test
	void testCreateLocatario_EmailIsTaken() throws Exception {
		service.registerLocatario(userDTO);
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(post("/locatarios")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetLocatarioById_existentId() throws Exception {
		Locatario locatario = service.registerLocatario(userDTO);

		servlet.perform(get("/locatarios/" + locatario.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(locatario.getUser().getEmail())))
				.andExpect(jsonPath("user.firstName", is(locatario.getUser().getFirstName())))
				.andExpect(jsonPath("user.lastName", is(locatario.getUser().getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(locatario.getUser().getPhoneNumber())))
				.andExpect(jsonPath("user.lastLogin", is(locatario.getUser().getLastLogin())))
				.andExpect(jsonPath("user.sex", is(locatario.getUser().getSex())))
				.andExpect(jsonPath("user.photo", is(locatario.getUser().getPhoto())))
				.andExpect(jsonPath("role", is(locatario.getRole())));
	}

	@Test
	void testGetLocatarioById_inexistentId() throws Exception {
		servlet.perform(get("/locatarios/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteLocatarioById_existentId() throws Exception {
		Locatario locatario = service.registerLocatario(userDTO);

		servlet.perform(delete("/locatarios/" + locatario.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void testDeleteLocatarioById_inexistentId() throws Exception {
		servlet.perform(delete("/locatarios/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateLocatario_locatarioDoesNotExist() throws Exception {
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(put("/locatarios/0")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateLocatario_completeUpdate() throws Exception {
		Locatario locatario = service.registerLocatario(userDTO);

		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(put("/locatarios/" + locatario.getId())
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(locatario.getUser().getEmail())))
				.andExpect(jsonPath("user.firstName", is(locatario.getUser().getFirstName())))
				.andExpect(jsonPath("user.lastName", is(locatario.getUser().getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(locatario.getUser().getPhoneNumber())))
				.andExpect(jsonPath("user.lastLogin", is(locatario.getUser().getLastLogin())))
				.andExpect(jsonPath("user.sex", is(locatario.getUser().getSex())))
				.andExpect(jsonPath("user.photo", is(locatario.getUser().getPhoto())))
				.andExpect(jsonPath("role", is(locatario.getRole())));
	}

	@Test
	void testUpdateLocatario_partialUpdate() throws Exception {
		Locatario locatario = service.registerLocatario(userDTO);
		UserDTO updatedUserDTO = new UserDTO(null, null, null, null, null, null, "photo1");


		String userJsonString = mapper.writeValueAsString(updatedUserDTO);

		servlet.perform(put("/locatarios/" + locatario.getId())
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(locatario.getUser().getEmail())))
				.andExpect(jsonPath("user.firstName", is(locatario.getUser().getFirstName())))
				.andExpect(jsonPath("user.lastName", is(locatario.getUser().getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(locatario.getUser().getPhoneNumber())))
				.andExpect(jsonPath("user.lastLogin", is(locatario.getUser().getLastLogin())))
				.andExpect(jsonPath("user.sex", is(locatario.getUser().getSex())))
				.andExpect(jsonPath("user.photo", is(updatedUserDTO.getPhoto())))
				.andExpect(jsonPath("role", is(locatario.getRole())));
	}

}
