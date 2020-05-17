package tqs.domus.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.LocadorDTO;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.HouseRepository;
import tqs.domus.restapi.repository.LocadorRepository;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
	private HouseRepository repository;

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
	void testSearchHouse(){
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

		when(repository.save(any(House.class))).thenReturn(house);
		when(repository.findByAttributesAscPrice(anyString(), anyInt(), anyDouble(), anyDouble())).thenReturn(houses);
		assertThat(house.toString(), hasToString(houses.get(0).toString()));
	}

}
