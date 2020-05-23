package tqs.domus.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.HouseReview;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.HouseRepository;
import tqs.domus.restapi.repository.HouseReviewRepository;
import tqs.domus.restapi.repository.LocadorRepository;
import tqs.domus.restapi.repository.LocatarioRepository;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 16:10
 */

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

	@Mock(lenient = true)
	private LocadorRepository locadorRepository;

	@Mock(lenient = true)
	private LocatarioRepository locatarioRepository;

	@Mock(lenient = true)
	private HouseRepository repository;

	@Mock(lenient = true)
	private HouseReviewRepository reviewRepository;

	@InjectMocks
	private HouseService service;

	@Test
	void testRegisterHouse_incorrectParameters() {
		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);

		Locador locador = new Locador();
		locador.setUser(user);

		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300,
				true,
				230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);

		when(repository.save(any(House.class))).thenThrow(ConstraintViolationException.class);

		assertThrows(ErrorDetails.class, () -> {
			service.registerHouse(houseDTO);
		});
	}

	@Test
	void testRegisterHouse_missingLocadorParameter() {
		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);

		Locador locador = new Locador();
		locador.setUser(user);

		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300,
				true,
				230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);

		when(repository.save(any(House.class))).thenThrow(NullPointerException.class);

		assertThrows(ErrorDetails.class, () -> {
			service.registerHouse(houseDTO);
		});
	}

	@Test
	void testRegisterHouse_correctParameters() throws ErrorDetails, ResourceNotFoundException {
		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300,
				true,
				230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);

		House house = new ModelMapper().map(houseDTO, House.class);

		when(repository.save(any(House.class))).thenReturn(house);

		House result = service.registerHouse(houseDTO);
		assertThat(house.toString(), hasToString(result.toString()));
	}

	@Test
	void testRegisterHouse_locadorDoesNotExist() {
		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);

		Locador locador = new Locador();
		locador.setUser(user);

		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.empty());

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300,
				true,
				230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);


		assertThrows(ResourceNotFoundException.class, () -> {
			service.registerHouse(houseDTO);
		});
	}

	@Test
	void testUpdateHouse_houseDoesNotExist() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		HouseDTO houseDTO = new HouseDTO();

		assertThrows(ResourceNotFoundException.class, () -> {
			service.updateHouse(0L, houseDTO);
		});
	}

	@Test
	void testUpdateHouse_completeUpdate() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);

		HouseDTO updatedHouseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 3, 2, 2, 300,
				true, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House updatedHouse = new ModelMapper().map(updatedHouseDTO, House.class);

		when(repository.findById(anyLong())).thenReturn(Optional.of(house));
		when(repository.save(any(House.class))).thenReturn(updatedHouse);

		House result = service.updateHouse(1L, houseDTO);
		assertThat(updatedHouse.toString(), hasToString(result.toString()));
	}

	@Test
	void testSearchHouse_priceAsc() {
		List<House> houses = new ArrayList<>();

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		HouseDTO houseDTO2 = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 330, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house2 = new ModelMapper().map(houseDTO, House.class);
		houses.add(house2);

		when(repository.save(any(House.class))).thenReturn(house);
		when(repository.findByAttributesDescPrice(anyString(), anyInt(), anyDouble(), anyDouble())).thenReturn(houses);
		List<House> result = service.searchHouse("Aveiro", 5, 100.0, 500.0, "price", true);
		assertThat(houses.toString(), hasToString(result.toString()));
	}

	@Test
	void testSearchHouse_priceDesc() {
		List<House> houses = new ArrayList<>();

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);
		houses.add(house);
		HouseDTO houseDTO2 = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 330, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house2 = new ModelMapper().map(houseDTO, House.class);
		houses.add(house2);

		when(repository.save(any(House.class))).thenReturn(house);
		when(repository.findByAttributesAscPrice(anyString(), anyInt(), anyDouble(), anyDouble())).thenReturn(houses);
		List<House> result = service.searchHouse("Aveiro", 5, 100.0, 500.0, "price", false);
		assertThat(houses.toString(), hasToString(result.toString()));
	}

	@Test
	void testSearchHouse_ratingAsc() {
		List<House> houses = new ArrayList<>();

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);
		house.setAverageRating(5.0);
		houses.add(house);
		HouseDTO houseDTO2 = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 330, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house2 = new ModelMapper().map(houseDTO, House.class);
		house2.setAverageRating(3.5);
		houses.add(house2);

		when(repository.save(any(House.class))).thenReturn(house);
		when(repository.findByAttributesAscRating(anyString(), anyInt(), anyDouble(), anyDouble())).thenReturn(houses);
		List<House> result = service.searchHouse("Aveiro", 5, 100.0, 500.0, "rating", false);
		assertThat(houses.toString(), hasToString(result.toString()));
	}

	@Test
	void testSearchHouse_ratingDesc() {
		List<House> houses = new ArrayList<>();

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);
		house.setAverageRating(5.0);
		houses.add(house);
		HouseDTO houseDTO2 = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 330, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house2 = new ModelMapper().map(houseDTO, House.class);
		house2.setAverageRating(3.5);
		houses.add(house2);

		when(repository.save(any(House.class))).thenReturn(house);
		when(repository.findByAttributesDescRating(anyString(), anyInt(), anyDouble(), anyDouble())).thenReturn(houses);
		List<House> result = service.searchHouse("Aveiro", 5, 100.0, 500.0, "rating", true);
		assertThat(houses.toString(), hasToString(result.toString()));
	}

	@Test
	void testGetAllCities_isEmpty() {
		when(repository.findAllCities()).thenReturn(Collections.emptyList());
		assertThat(service.getAllCities(), is(empty()));
	}

	@Test
	void testGetAllCities_isNotEmpty() {
		List<String> cities = new ArrayList<>();
		cities.add("Aveiro");
		cities.add("Viseu");

		when(repository.findAllCities()).thenReturn(cities);

		List<String> result = service.getAllCities();

		assertThat(cities, is(not(empty())));
		assertThat(cities, is(equalTo(result)));
	}

	@Test
	void testGetHouse_houseDoesNotExist() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.getHouse(0L);
		});
	}

	@Test
	void testGetHouse_houseExists() throws ResourceNotFoundException {

		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);

		when(repository.findById(anyLong())).thenReturn(Optional.of(house));
		when(repository.save(any(House.class))).thenReturn(house);

		House result = service.getHouse(1L);
		assertThat(house.toString(), hasToString(result.toString()));
	}

	@Test
	void testDeleteHouse_houseDoesNotExists() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteHouse(anyLong());
		});
	}

	@Test
	void testDeleteHouse_completeDelete() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);

		when(repository.findById(anyLong())).thenReturn(Optional.of(house));
		when(repository.save(any(House.class))).thenReturn(house);
		ResponseEntity<Void> result = service.deleteHouse(house.getId());
		assertThat(ResponseEntity.noContent().build().toString(), hasToString(result.toString()));
	}

	@Test
	void testDeleteHouseReview_reviewDoesNotExist() {
		when(reviewRepository.findByHouseIdAndLocatarioId(anyLong(), anyLong())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteHouseReview(anyLong(), anyLong());
		});
	}

	@Test
	void testDeleteHouseReview_reviewExists() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		LocadorDTO locadorDTO = new LocadorDTO(user.getId());

		userDTO = new UserDTO("v1@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		List<String> photos = new ArrayList<>() {{
			add("photo1");
		}};

		when(locadorRepository.findById(anyLong())).thenReturn(Optional.of(locador));

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", photos, locadorDTO);
		House house = new ModelMapper().map(houseDTO, House.class);

		when(repository.findById(anyLong())).thenReturn(Optional.of(house));
		when(repository.save(any(House.class))).thenReturn(house);

		HouseReview review = new HouseReview();
		review.setLocatario(locatario);
		review.setHouse(house);
		review.setComment("comment");
		review.setRating(0D);

		List<HouseReview> reviews = new ArrayList<>() {{
			add(review);
		}};

		house.setReviewsReceived(reviews);

		when(reviewRepository.findByHouseIdAndLocatarioId(anyLong(), anyLong())).thenReturn(Optional.of(review));

		ResponseEntity<Void> result = service.deleteHouseReview(house.getId(), locatario.getId());

		assertThat(ResponseEntity.noContent().build().toString(), hasToString(result.toString()));
	}

}
