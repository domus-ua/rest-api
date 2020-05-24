package tqs.domus.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.HouseReview;
import tqs.domus.restapi.model.HouseReviewDTO;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.HouseService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

	@AfterEach
	void resetService() {
		reset(service);
	}

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
				.andExpect(jsonPath("noRooms", is(house.getNoRooms())))
				.andExpect(jsonPath("noBathrooms", is(house.getNoBathrooms())))
				.andExpect(jsonPath("noGarages", is(house.getNoGarages())))
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
				.andExpect(jsonPath("noRooms", is(house.getNoRooms())))
				.andExpect(jsonPath("noBathrooms", is(house.getNoBathrooms())))
				.andExpect(jsonPath("noGarages", is(house.getNoGarages())))
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
	void testSearchHouse_incorrectParameters() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(house);
		given(service.searchHouse(anyString(), anyInt(), anyDouble(), anyDouble(), anyString(), anyBoolean())).willReturn(houses);
		servlet.perform(get("/houses")
				.param("orderAttribute", "wrong")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		reset(service);
	}

	@Test
	void testSearchHouse_Ok() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(house);
		given(service.searchHouse(anyString(), anyInt(), anyDouble(), anyDouble(), anyString(), anyBoolean())).willReturn(houses);
		servlet.perform(get("/houses")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testSearchHouse_PriceOrder() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(house);
		given(service.searchHouse(anyString(), anyInt(), anyDouble(), anyDouble(), anyString(), anyBoolean())).willReturn(houses);
		servlet.perform(get("/houses")
				.param("orderAttribute", "price")
				.param("desc", "true")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testSearchHouse_RatingOrder() throws Exception {
		List<House> houses = new ArrayList<>();
		House house = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		String houseJsonString = mapper.writeValueAsString(house);
		given(service.searchHouse(anyString(), anyInt(), anyDouble(), anyDouble(), anyString(), anyBoolean())).willReturn(houses);
		servlet.perform(get("/houses")
				.param("orderAttribute", "rating")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testGetHouse_houseDoesNotExist() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		given(service.getHouse(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/houses/1")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testGetHouse_houseExists() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);
		String houseJsonString = mapper.writeValueAsString(house);

		given(service.getHouse(anyLong())).willReturn(house);

		servlet.perform(get("/houses/1")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		reset(service);
	}

	@Test
	void testDeleteHouse_houseDoesNotExist() throws Exception {
		given(service.deleteHouse(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(delete("/houses/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteHouse_houseExists() throws Exception {
		given(service.deleteHouse(anyLong())).willReturn(ResponseEntity.noContent().build());

		servlet.perform(delete("/houses/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void testCreateHouseReview_houseDoesNotExist() throws Exception {
		HouseReviewDTO houseReviewDTO = new HouseReviewDTO(0L, 0L, "string", 0D);
		String houseReviewJsonString = mapper.writeValueAsString(houseReviewDTO);

		given(service.registerReview(any(HouseReviewDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(post("/houses/reviews")
				.content(houseReviewJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateHouseReview_locatarioDoesNotExist() throws Exception {
		HouseReviewDTO houseReviewDTO = new HouseReviewDTO(0L, 0L, "string", 0D);
		String houseReviewJsonString = mapper.writeValueAsString(houseReviewDTO);

		given(service.registerReview(any(HouseReviewDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(post("/houses/reviews")
				.content(houseReviewJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateHouseReview_missingParameters() throws Exception {
		HouseReviewDTO houseReviewDTO = new HouseReviewDTO(0L, 0L, "string", 0D);
		String houseReviewJsonString = mapper.writeValueAsString(houseReviewDTO);

		given(service.registerReview(any(HouseReviewDTO.class))).willThrow(new ErrorDetails("Error"));

		servlet.perform(post("/houses/reviews")
				.content(houseReviewJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateHouseReview_correctInput() throws Exception {
		HouseReviewDTO reviewDTO2 = new HouseReviewDTO(0L, 0L, "Review", 3.0);
		String houseJsonString = mapper.writeValueAsString(reviewDTO2);
		HouseReview review = new ModelMapper().map(reviewDTO2, HouseReview.class);

		given(service.registerReview(any(HouseReviewDTO.class))).willReturn(review);

		servlet.perform(post("/houses/reviews")
				.content(houseJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("comment", is(reviewDTO2.getComment())))
				.andExpect(jsonPath("rating", is(reviewDTO2.getRating())));
	}

	@Test
	void testGetHouseReviews_houseExists() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);

		given(service.getHouseReviews(anyLong())).willReturn(house.getReviewsReceived());

		servlet.perform(get("/houses/reviews/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testGetHouseReviews_houseDoesNotExist() throws Exception {
		given(service.getHouseReviews(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/houses/reviews/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetHouseReview_houseDoesNotExist() throws Exception {
		given(service.getHouseReview(anyLong(), anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/houses/reviews/0/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetHouseReview_locatarioDoesNotExist() throws Exception {
		given(service.getHouseReview(anyLong(), anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/houses/reviews/0/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetHouseReview_houseAndLocatarioExists() throws Exception {
		House house = new ModelMapper().map(houseDTO, House.class);

		given(service.getHouseReviews(anyLong())).willReturn(house.getReviewsReceived());

		servlet.perform(get("/houses/reviews/1/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testUpdateHouseReview_reviewDoesNotExist() throws Exception {
		given(service.updateHouseReview(any(HouseReviewDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		HouseReviewDTO reviewDTO2 = new HouseReviewDTO(0L, 0L, null, null);
		String houseReviewJsonString = mapper.writeValueAsString(reviewDTO2);

		servlet.perform(put("/houses/reviews/")
				.content(houseReviewJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateHouseReview_noUpdate() throws Exception {
		HouseReviewDTO reviewDTO2 = new HouseReviewDTO(0L, 0L, "Review", 3.0);
		HouseReview review = new ModelMapper().map(reviewDTO2, HouseReview.class);

		given(service.updateHouseReview(any(HouseReviewDTO.class))).willReturn(review);

		String houseReviewJsonString = mapper.writeValueAsString(reviewDTO2);

		servlet.perform(put("/houses/reviews/")
				.content(houseReviewJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testUpdateHouseReview_completeUpdate() throws Exception {
		HouseReviewDTO reviewDTO2 = new HouseReviewDTO(0L, 0L, "Review", 3.0);
		HouseReview review = new ModelMapper().map(reviewDTO2, HouseReview.class);

		given(service.updateHouseReview(any(HouseReviewDTO.class))).willReturn(review);

		String houseReviewJsonString = mapper.writeValueAsString(reviewDTO2);

		servlet.perform(put("/houses/reviews/")
				.content(houseReviewJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testDeleteHouseReview_reviewDoesNotExist() throws Exception {
		given(service.deleteHouseReview(anyLong(), anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(delete("/houses/reviews/0/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteHouseReview_reviewExists() throws Exception {
		given(service.deleteHouseReview(anyLong(), anyLong())).willReturn(ResponseEntity.noContent().build());

		servlet.perform(delete("/houses/reviews/1/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

}
