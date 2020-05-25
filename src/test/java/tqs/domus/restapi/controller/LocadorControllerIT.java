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
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;
import tqs.domus.restapi.service.HouseService;
import tqs.domus.restapi.service.LocadorService;
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
public class LocadorControllerIT {

	private final ObjectMapper mapper = new ObjectMapper();

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

	@Autowired
	private MockMvc servlet;

	@Autowired
	private UserRepository repository;

	@Autowired
	private LocadorService service;

	@Autowired
	private LocatarioService locatarioService;

	@Autowired
	private HouseService houseService;

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	void testCreateLocador_EmailIsFree() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(post("/locadores")
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
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));
	}

	@Test
	void testCreateLocador_EmailIsTaken() throws Exception {
		service.registerLocador(userDTO);
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(post("/locadores")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetLocadorById_inexistentId() throws Exception {
		servlet.perform(get("/locadores/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetLocadorById_existentId() throws Exception {
		Locador locador = service.registerLocador(userDTO);

		servlet.perform(get("/locadores/" + locador.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(locador.getUser().getEmail())))
				.andExpect(jsonPath("user.firstName", is(locador.getUser().getFirstName())))
				.andExpect(jsonPath("user.lastName", is(locador.getUser().getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(locador.getUser().getPhoneNumber())))
				.andExpect(jsonPath("user.lastLogin", is(locador.getUser().getLastLogin())))
				.andExpect(jsonPath("user.sex", is(locador.getUser().getSex())))
				.andExpect(jsonPath("user.photo", is(locador.getUser().getPhoto())))
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));
	}

	@Test
	void testDeleteLocadorById_existentId() throws Exception {

		Locador locador = service.registerLocador(userDTO);

		servlet.perform(delete("/locadores/" + locador.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void testDeleteLocadorById_inexistentId() throws Exception {
		servlet.perform(delete("/locadores/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateLocador_locadorDoesNotExist() throws Exception {
		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(put("/locadores/0")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateLocador_completeUpdate() throws Exception {
		Locador locador = service.registerLocador(userDTO);

		String userJsonString = mapper.writeValueAsString(userDTO);

		servlet.perform(put("/locadores/" + locador.getId())
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(locador.getUser().getEmail())))
				.andExpect(jsonPath("user.firstName", is(locador.getUser().getFirstName())))
				.andExpect(jsonPath("user.lastName", is(locador.getUser().getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(locador.getUser().getPhoneNumber())))
				.andExpect(jsonPath("user.lastLogin", is(locador.getUser().getLastLogin())))
				.andExpect(jsonPath("user.sex", is(locador.getUser().getSex())))
				.andExpect(jsonPath("user.photo", is(locador.getUser().getPhoto())))
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));
	}

	@Test
	void testUpdateLocador_partialUpdate() throws Exception {
		Locador locador = service.registerLocador(userDTO);
		UserDTO updatedUserDTO = new UserDTO(null, null, null, null, null, null, "photo1");


		String userJsonString = mapper.writeValueAsString(updatedUserDTO);

		servlet.perform(put("/locadores/" + locador.getId())
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("user.email", is(locador.getUser().getEmail())))
				.andExpect(jsonPath("user.firstName", is(locador.getUser().getFirstName())))
				.andExpect(jsonPath("user.lastName", is(locador.getUser().getLastName())))
				.andExpect(jsonPath("user.phoneNumber", is(locador.getUser().getPhoneNumber())))
				.andExpect(jsonPath("user.lastLogin", is(locador.getUser().getLastLogin())))
				.andExpect(jsonPath("user.sex", is(locador.getUser().getSex())))
				.andExpect(jsonPath("user.photo", is(updatedUserDTO.getPhoto())))
				.andExpect(jsonPath("role", is(locador.getRole())))
				.andExpect(jsonPath("verified", is(locador.isVerified())));
	}


	@Test
	void testGetLocadorHouses_locadorDoesNotExist() throws Exception {
		servlet.perform(get("/locadores/houses/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetLocadorHouses_locadorExists() throws Exception {
		Locador locador = service.registerLocador(userDTO);

		servlet.perform(get("/locadores/houses/" + locador.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testCheckQualityParameter_locadorDoesNotExist() throws Exception {
		servlet.perform(get("/locadores/check-quality/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCheckQualityParameter_locadorExists() throws Exception {
		Locador locador = service.registerLocador(userDTO);

		servlet.perform(get("/locadores/check-quality/" + locador.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testRentHouse_locadorDoesNotExist() throws Exception {
		String jsonString = "{\"locatarioId\": 0, \"locadorId\": 0, \"houseId\": 0, \"startDate\": \"2020-05-25\", " +
				"\"endDate\": \"2020-05-26\", \"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testRentHouse_houseDoesNotExist() throws Exception {
		Locador locador = service.registerLocador(userDTO);
		String jsonString = "{\"locatarioId\": 0, \"locadorId\": " + locador.getId() + ", \"houseId\": 0, " +
				"\"startDate\": \"2020-05-25\", \"endDate\": \"2020-05-26\", \"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testRentHouse_locatarioDoesNotExist() throws Exception {
		Locador locador = service.registerLocador(userDTO);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());
		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
		House house = houseService.registerHouse(houseDTO);
		String jsonString = "{\"locatarioId\": 0, \"locadorId\": " + locador.getId() + ", \"houseId\": " +
				house.getId() + ", \"startDate\": \"2020-05-25\", \"endDate\": \"2020-05-26\", \"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testRentHouse_AllEntitiesExist() throws Exception {
		Locador locador = service.registerLocador(userDTO);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
		House house = houseService.registerHouse(houseDTO);

		UserDTO userDTO2 = new UserDTO("v1@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		Locatario locatario = locatarioService.registerLocatario(userDTO2);

		String jsonString = "{\"locatarioId\": " + locatario.getId() + ", \"locadorId\": " + locador.getId() + ", \"houseId" +
				"\": " + house.getId() + ", \"startDate\": \"2020-05-25\", \"endDate\": \"2020-05-26\", \"price\": " +
				"550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testRentHouse_DateIsWrong() throws Exception {
		Locador locador = service.registerLocador(userDTO);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
		House house = houseService.registerHouse(houseDTO);

		UserDTO userDTO2 = new UserDTO("v1@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		Locatario locatario = locatarioService.registerLocatario(userDTO2);

		String jsonString = "{\"locatarioId\": " + locatario.getId() + ", \"locadorId\": " + locador.getId() + ", \"houseId" +
				"\": " + house.getId() + ", \"startDate\": \"2020-05-25\", \"endDate\": \"2020-04-26\", \"price\": " +
				"550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testRentHouse_HouseisNotAssociatedWithLocador() throws Exception {
		Locador locador = service.registerLocador(userDTO);
		UserDTO userDTO2 = new UserDTO("v1@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		Locador locador2 = service.registerLocador(userDTO2);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
		House house = houseService.registerHouse(houseDTO);

		UserDTO userDTO3 = new UserDTO("v2@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		Locatario locatario = locatarioService.registerLocatario(userDTO3);

		String jsonString = "{\"locatarioId\": " + locatario.getId() + ", \"locadorId\": " + locador2.getId() + ", " +
				"\"houseId\": " + house.getId() + ", \"startDate\": \"2020-05-25\", \"endDate\": \"2020-05-26\", " +
				"\"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testRentHouse_HouseisNotAvailable() throws Exception {
		Locador locador = service.registerLocador(userDTO);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, false
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
		House house = houseService.registerHouse(houseDTO);

		UserDTO userDTO3 = new UserDTO("v2@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		Locatario locatario = locatarioService.registerLocatario(userDTO3);

		String jsonString = "{\"locatarioId\": " + locatario.getId() + ", \"locadorId\": " + locador.getId() + ", " +
				"\"houseId\": " + house.getId() + ", \"startDate\": \"2020-05-25\", \"endDate\": \"2020-05-26\", " +
				"\"price\": 550.0}";

		servlet.perform(post("/locadores/rent")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
