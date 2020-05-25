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
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.LocadorRepository;
import tqs.domus.restapi.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 22:01
 */

@ExtendWith(MockitoExtension.class)
public class LocadorServiceTest {

	@Mock(lenient = true)
	private LocadorRepository repository;

	@Mock(lenient = true)
	private UserRepository userRepository;

	@InjectMocks
	private LocadorService service;


	@Test
	void testRegisterLocador_emailIsTaken() {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		assertThrows(ErrorDetails.class, () -> {
			service.registerLocador(userDTO);
		});
	}

	@Test
	void testRegisterLocador_emailIsFree() throws ErrorDetails {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		user.setLocador(locador);

		when(repository.save(any(Locador.class))).thenReturn(locador);

		Locador result = service.registerLocador(userDTO);
		assertThat(locador.toString(), hasToString(result.toString()));
	}

	@Test
	void testRegisterLocador_missingParameters() throws ErrorDetails {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", null, "123", "M", null);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);
		user.setLocador(locador);

		when(repository.save(any(Locador.class))).thenThrow(ConstraintViolationException.class);

		assertThrows(ErrorDetails.class, () -> {
			service.registerLocador(userDTO);
		});
	}

	@Test
	void testGetLocadorById_inexistentId() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.getLocadorById(0L);
		});
	}

	@Test
	void testGetLocadorById_existentId() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locador));

		Locador result = service.getLocadorById(0L);

		assertThat(locador.toString(), hasToString(result.toString()));
	}

	@Test
	void testDeleteLocadorById_inexistentId() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteLocadorById(0L);
		});

	}

	@Test
	void testDeleteLocadorById_existentId() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locador));

		ResponseEntity<Void> result = service.deleteLocadorById(0L);

		assertThat(ResponseEntity.noContent().build(), hasToString(result.toString()));
	}

	@Test
	void testUpdateLocador_locadorDoesNotExist() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		assertThrows(ResourceNotFoundException.class, () -> {
			service.updateLocadorById(0L, userDTO);
		});
	}

	@Test
	void testUpdateLocador_completeUpdate() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		UserDTO updatedUserDTO = new UserDTO("v@ua.pt", "João", "Vasconcelos", "pwd", "123", "M", null);

		User updatedUser = new ModelMapper().map(updatedUserDTO, User.class);
		Locador updatedLocador = new Locador();
		updatedLocador.setUser(updatedUser);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locador));
		when(repository.save(any(Locador.class))).thenReturn(updatedLocador);

		Locador result = service.updateLocadorById(1L, updatedUserDTO);
		assertThat(updatedLocador.toString(), hasToString(result.toString()));
	}

	@Test
	void testUpdateLocador_partialUpdate() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		UserDTO updatedUserDTO = new UserDTO(null, null, null, null, null, null, "photo1");

		User updatedUser = new ModelMapper().map(updatedUserDTO, User.class);
		Locador updatedLocador = new Locador();
		updatedLocador.setUser(updatedUser);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locador));
		when(repository.save(any(Locador.class))).thenReturn(updatedLocador);

		Locador result = service.updateLocadorById(1L, updatedUserDTO);
		assertThat(updatedLocador.toString(), hasToString(result.toString()));
	}

	@Test
	void testCheckQualityParameter_locadorDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> {
			service.checkQualityParameter(0L);
		});
	}

	@Test
	void testCheckQualityParameter_locadorExistsAndNoReviews() throws Exception {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locador));

		String result = service.checkQualityParameter(1L);

		assertThat("Your request was denied!", equalTo(result));
	}

	@Test
	void testCheckQualityParameter_locadorExistsAndReviewsEmpty() throws Exception {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		List<House> houses = new ArrayList<>() {{
			add(new House());
		}};
		locador.setHouses(houses);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locador));

		String result = service.checkQualityParameter(1L);

		assertThat("Your request was denied!", equalTo(result));
	}

	@Test
	void testCheckQualityParameter_locadorExistsAndIsAllowedToBeVerified() throws Exception {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locador locador = new Locador();
		locador.setUser(user);

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, null);
		House house = new ModelMapper().map(houseDTO, House.class);
		house.setAverageRating(4.3);

		List<House> houses = new ArrayList<>() {{
			add(house);
		}};
		locador.setHouses(houses);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locador));

		String result = service.checkQualityParameter(1L);

		assertThat("Your request was accepted! Your quality is now verified!", equalTo(result));
	}

}
