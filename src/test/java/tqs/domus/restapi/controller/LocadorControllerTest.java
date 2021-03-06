package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.Contract;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.RentDTO;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.LocadorService;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
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

@WebMvcTest(value = LocadorController.class)
public class LocadorControllerTest {

	private final ObjectMapper mapper = new ObjectMapper();

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

	@Autowired
	private MockMvc servlet;

	@MockBean
	private LocadorService service;

	@Test
	void testCreateLocador_EmailIsFree() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.registerLocador(any(UserDTO.class))).willReturn(locador);

		servlet.perform(post("/locadores")
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
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));

		reset(service);
	}

	@Test
	void testCreateLocador_EmailIsTaken() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.registerLocador(any(UserDTO.class))).willThrow(new ErrorDetails("Error"));

		servlet.perform(post("/locadores")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		reset(service);
	}

	@Test
	void testGetLocadorById_inexistentId() throws Exception {
		given(service.getLocadorById(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/locadores/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testGetLocadorById_existentId() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		given(service.getLocadorById(anyLong())).willReturn(locador);

		servlet.perform(get("/locadores/1")
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
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));

		reset(service);
	}

	@Test
	void testDeleteLocadorById_inexistentId() throws Exception {
		given(service.deleteLocadorById(anyLong())).willReturn(ResponseEntity.noContent().build());

		servlet.perform(delete("/locadores/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		reset(service);

	}

	@Test
	void testDeleteLocadorById_existentId() throws Exception {
		given(service.deleteLocadorById(anyLong())).willThrow(new ResourceNotFoundException("Error"));


		servlet.perform(delete("/locadores/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testUpdateLocador_locadorDoesNotExist() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.updateLocadorById(anyLong(), any(UserDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(put("/locadores/1")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testUpdateLocador_completeUpdate() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.updateLocadorById(anyLong(), any(UserDTO.class))).willReturn(locador);

		servlet.perform(put("/locadores/1")
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
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));

		reset(service);

	}

	@Test
	void testUpdateLocador_partialUpdate() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		UserDTO updatedUserDTO = new UserDTO(null, null, null, null, null, null, "photo1");
		String userJsonString = mapper.writeValueAsString(updatedUserDTO);

		given(service.updateLocadorById(anyLong(), any(UserDTO.class))).willReturn(locador);

		servlet.perform(put("/locadores/1")
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
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));

		reset(service);
	}

	@Test
	void testGetLocadorHouses_locadorDoesNotExist() throws Exception {
		given(service.getLocadorHouses(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/locadores/houses/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testGetLocadorHouses_locadorExists() throws Exception {
		given(service.getLocadorHouses(anyLong())).willReturn(new ArrayList<>());

		servlet.perform(get("/locadores/houses/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testCheckQualityParameter_locadorDoesNotExist() throws Exception {
		given(service.checkQualityParameter(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/locadores/check-quality/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testCheckQualityParameter_locadorExists() throws Exception {
		given(service.checkQualityParameter(anyLong())).willReturn("Passed!");

		servlet.perform(get("/locadores/check-quality/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testRentHouse_someEntityDoesNotExist() throws Exception {
		given(service.rentHouse(any(RentDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		String jsonString = "{\"locatarioEmail\": 0, \"locadorId\": 0, \"houseId\": 0, \"startDate\": \"2020-05-25\", " +
				"\"endDate\": \"2020-05-26\", \"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testRentHouse_AllEntitiesExist() throws Exception {
		given(service.rentHouse(any(RentDTO.class))).willReturn(new Contract());

		String jsonString = "{\"locatarioEmail\": 0, \"locadorId\": 0, \"houseId\": 0, \"startDate\": \"2020-05-25\", " +
				"\"endDate\": \"2020-05-26\", \"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testRentHouse_ErrorInParameters() throws Exception {
		given(service.rentHouse(any(RentDTO.class))).willThrow(new ErrorDetails("Error"));

		String jsonString = "{\"locatarioEmail\": 0, \"locadorId\": 0, \"houseId\": 0, \"startDate\": \"2020-05-25\", " +
				"\"endDate\": \"2020-05-26\", \"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		reset(service);
	}
}
