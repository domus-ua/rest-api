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
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.model.WishListDTO;
import tqs.domus.restapi.service.HouseService;
import tqs.domus.restapi.service.LocatarioService;

import java.util.HashSet;
import java.util.Set;

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

@WebMvcTest(value = LocatarioController.class)
public class LocatarioControllerTest {
	private final ObjectMapper mapper = new ObjectMapper();

	private final UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

	@Autowired
	private MockMvc servlet;

	@MockBean
	private LocatarioService service;

	@MockBean
	private HouseService houseService;

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
				.andExpect(jsonPath("role", is(locatario.getRole())));

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
				.andExpect(jsonPath("role", is(locatario.getRole())));

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

	@Test
	void testDeleteLocatarioById_existentId() throws Exception {

		given(service.deleteLocatarioById(anyLong())).willReturn(ResponseEntity.noContent().build());

		servlet.perform(delete("/locatarios/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void testUpdateLocatario_locatarioDoesNotExist() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.updateLocatarioById(anyLong(), any(UserDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(put("/locatarios/1")
				.content(userJsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testDeleteLocatarioById_inexistentId() throws Exception {
		given(service.deleteLocatarioById(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(delete("/locatarios/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		reset(service);
	}

	@Test
	void testUpdateLocatario_completeUpdate() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);
		String userJsonString = mapper.writeValueAsString(user);

		given(service.updateLocatarioById(anyLong(), any(UserDTO.class))).willReturn(locatario);

		servlet.perform(put("/locatarios/1")
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
				.andExpect(jsonPath("role", is(locatario.getRole())));

		reset(service);
	}

	@Test
	void testUpdateLocatario_partialUpdate() throws Exception {
		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		UserDTO updatedUserDTO = new UserDTO(null, null, null, null, null, null, "photo1");
		String userJsonString = mapper.writeValueAsString(updatedUserDTO);

		given(service.updateLocatarioById(anyLong(), any(UserDTO.class))).willReturn(locatario);

		servlet.perform(put("/locatarios/1")
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
				.andExpect(jsonPath("role", is(locatario.getRole())));

		reset(service);
	}

	@Test
	void testAddHouseToWishList_houseDoesNotExist() throws Exception {
		given(service.addToWishlist(any(WishListDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		WishListDTO wishListDTO = new WishListDTO(1L, 0L);

		String jsonString = mapper.writeValueAsString(wishListDTO);

		servlet.perform(post("/locatarios/wishlist")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testAddHouseToWishList_locatarioDoesNotExist() throws Exception {
		given(service.addToWishlist(any(WishListDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		WishListDTO wishListDTO = new WishListDTO(0L, 1L);

		String jsonString = mapper.writeValueAsString(wishListDTO);

		servlet.perform(post("/locatarios/wishlist")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testAddHouseToWishList_locatarioAndHouseExist() throws Exception {
		WishListDTO wishListDTO = new WishListDTO(1L, 1L);

		given(service.addToWishlist(any(WishListDTO.class))).willReturn(true);

		String jsonString = mapper.writeValueAsString(wishListDTO);

		servlet.perform(post("/locatarios/wishlist")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testGetWishList_locatarioDoesNotExist() throws Exception {
		given(service.getLocatarioWishlist(anyLong())).willThrow(new ResourceNotFoundException("Error"));

		servlet.perform(get("/locatarios/wishlist/0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetWishList_locatarioExists() throws Exception {
		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, null);
		House house = new ModelMapper().map(houseDTO, House.class);

		Set<House> wishlist = new HashSet<>() {{
			add(house);
		}};

		given(service.getLocatarioWishlist(anyLong())).willReturn(wishlist);

		servlet.perform(get("/locatarios/wishlist/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].street", is(house.getStreet())));
	}

	@Test
	void testDeleteHouseFromWishList_houseDoesNotExist() throws Exception {
		given(service.deleteFromWishlist(any(WishListDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		WishListDTO wishListDTO = new WishListDTO(1L, 0L);

		String jsonString = mapper.writeValueAsString(wishListDTO);

		servlet.perform(delete("/locatarios/wishlist")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteHouseFromWishList_locatarioDoesNotExist() throws Exception {
		given(service.deleteFromWishlist(any(WishListDTO.class))).willThrow(new ResourceNotFoundException("Error"));

		WishListDTO wishListDTO = new WishListDTO(0L, 1L);

		String jsonString = mapper.writeValueAsString(wishListDTO);

		servlet.perform(delete("/locatarios/wishlist")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteHouseFromWishList_locatarioAndHouseExist() throws Exception {
		WishListDTO wishListDTO = new WishListDTO(1L, 1L);

		given(service.deleteFromWishlist(any(WishListDTO.class))).willReturn(ResponseEntity.noContent().build());

		String jsonString = mapper.writeValueAsString(wishListDTO);

		servlet.perform(delete("/locatarios/wishlist")
				.content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
}
