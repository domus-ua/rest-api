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
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.HouseService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 16:10
 */

@WebMvcTest(value = HouseController.class)
public class HouseControllerTest {

	private final ObjectMapper mapper = new ObjectMapper();
	private final List<String> photos = new ArrayList<>() {{
		add("photo1");
	}};

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

	private final User user = new ModelMapper().map(userDTO, User.class);
	private final LocadorDTO locador = new LocadorDTO(user.getId());

	private final HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locador);

	@Autowired
	private MockMvc servlet;

	@MockBean
	private HouseService service;

	@Test
	void testCreateHouse_correctParameters() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		given(service.registerHouse(any(HouseDTO.class))).willReturn(house);

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
				.andExpect(jsonPath("photos", is(house.getPhotos())))
				.andExpect(jsonPath("locador", is(house.getLocador())));

		reset(service);
	}

	@Test
	void testCreateHouse_incorrectParameters() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		given(service.registerHouse(any(HouseDTO.class))).willThrow(new ErrorDetails("Error"));

		servlet.perform(post("/houses")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		reset(service);
	}


	@Test
	void testCreateHouse_locadorDoesNotExist() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		given(service.registerHouse(any(HouseDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(post("/houses")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testUpdateHouse_houseDoesNotExist() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		given(service.updateHouse(anyLong(), any(HouseDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(put("/houses/1")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testUpdateHouse_completeUpdate() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		given(service.updateHouse(anyLong(), any(HouseDTO.class))).willReturn(house);

		servlet.perform(put("/houses/1")
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
				.andExpect(jsonPath("photos", is(house.getPhotos())))
				.andExpect(jsonPath("locador", is(house.getLocador())));

		reset(service);
	}

}
