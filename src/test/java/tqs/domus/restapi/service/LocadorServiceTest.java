package tqs.domus.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.LocadorRepository;
import tqs.domus.restapi.repository.UserRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
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

}
