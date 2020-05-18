package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.domus.restapi.RestApiApplication;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;
import tqs.domus.restapi.service.HouseService;
import tqs.domus.restapi.service.LocadorService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 16:10
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestApiApplication.class)
@AutoConfigureMockMvc
public class HouseControllerIT {

	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private MockMvc servlet;

	@Autowired
	private UserRepository repository;

	@Autowired
	private LocadorService locadorService;

	@Autowired
	private HouseService houseService;

	private HouseDTO houseDTO;

	private Locador locador;


	@BeforeEach
	public void initDb() throws ErrorDetails {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		locador = locadorService.registerLocador(userDTO);
		LocadorDTO locadorDTO = new LocadorDTO(locador.getId());
		houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, locadorDTO);
	}

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	void testCreateHouse_correctParameters() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		house.setLocador(locador);
		String houseJsonString = mapper.writeValueAsString(house);

		servlet.perform(post("/houses")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("street", is(house.getStreet())))
				.andExpect(jsonPath("city", is(house.getCity())))
				.andExpect(jsonPath("postalCode", is(house.getPostalCode())))
				.andExpect(jsonPath("nrooms", is(house.getNRooms())))
				.andExpect(jsonPath("nbathrooms", is(house.getNBathrooms())))
				.andExpect(jsonPath("ngarages", is(house.getNGarages())))
				.andExpect(jsonPath("habitableArea", is(house.getHabitableArea())))
				.andExpect(jsonPath("price", is(house.getPrice())))
				.andExpect(jsonPath("name", is(house.getName())))
				.andExpect(jsonPath("description", is(house.getDescription())))
				.andExpect(jsonPath("propertyFeatures", is(house.getPropertyFeatures())))
				.andExpect(jsonPath("photos", is(house.getPhotos())));
	}

	@Test
	void testCreateHouse_incorrectParameters() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		servlet.perform(post("/houses")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}


	@Test
	void testCreateHouse_locadorDoesNotExist() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		house.setLocador(locador);
		String houseJsonString = mapper.writeValueAsString(house);

		repository.deleteAll();

		servlet.perform(post("/houses")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateHouse_houseDoesNotExist() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		servlet.perform(put("/houses/1")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateHouse_completeUpdate() throws Exception {
		House house = houseService.registerHouse(houseDTO);
		House houseMapper = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(houseMapper);

		servlet.perform(put("/houses/" + house.getId())
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("street", is(house.getStreet())))
				.andExpect(jsonPath("city", is(house.getCity())))
				.andExpect(jsonPath("postalCode", is(house.getPostalCode())))
				.andExpect(jsonPath("nrooms", is(house.getNRooms())))
				.andExpect(jsonPath("nbathrooms", is(house.getNBathrooms())))
				.andExpect(jsonPath("ngarages", is(house.getNGarages())))
				.andExpect(jsonPath("habitableArea", is(house.getHabitableArea())))
				.andExpect(jsonPath("price", is(house.getPrice())))
				.andExpect(jsonPath("name", is(house.getName())))
				.andExpect(jsonPath("description", is(house.getDescription())))
				.andExpect(jsonPath("propertyFeatures", is(house.getPropertyFeatures())));
	}

	@Test
	void testSearchHouse_incorrectParameters() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = houseService.registerHouse(houseDTO);
		House houseMapper = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(houseMapper);
		servlet.perform(get("/houses")
				.param("orderAttribute", "wrong")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testSearchHouse_Ok() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = houseService.registerHouse(houseDTO);
		House houseMapper = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(houseMapper);
		servlet.perform(get("/houses")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testSearchHouse_PriceOrder() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = houseService.registerHouse(houseDTO);
		House houseMapper = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(houseMapper);
		servlet.perform(get("/houses")
				.param("orderAttribute", "price")
				.param("desc", "true")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testSearchHouse_RatingOrder() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = houseService.registerHouse(houseDTO);
		House houseMapper = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(houseMapper);
		servlet.perform(get("/houses")
				.param("orderAttribute", "rating")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

}
