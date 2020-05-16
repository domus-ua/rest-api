package tqs.domus.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.LocatarioRepository;
import tqs.domus.restapi.repository.UserRepository;

import javax.validation.ConstraintViolationException;
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
public class LocatarioServiceTest {

	@Mock(lenient = true)
	private LocatarioRepository repository;

	@Mock(lenient = true)
	private UserRepository userRepository;

	@InjectMocks
	private LocatarioService service;


	@Test
	void testRegisterLocatario_emailIsTaken() {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		assertThrows(ErrorDetails.class, () -> {
			service.registerLocatario(userDTO);
		});
	}

	@Test
	void testRegisterLocatario_emailIsFree() throws ErrorDetails {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);
		user.setLocatario(locatario);

		when(repository.save(any(Locatario.class))).thenReturn(locatario);

		Locatario result = service.registerLocatario(userDTO);
		assertThat(locatario.toString(), hasToString(result.toString()));
	}

	@Test
	void testRegisterLocatario_missingParameters() throws ErrorDetails {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", null, "M", null);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);
		user.setLocatario(locatario);

		when(repository.save(any(Locatario.class))).thenThrow(ConstraintViolationException.class);

		assertThrows(ErrorDetails.class, () -> {
			service.registerLocatario(userDTO);
		});
	}

	@Test
	void testGetLocatarioById_inexistentId() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.getLocatarioById(0L);
		});
	}

	@Test
	void testGetLocatarioById_existentId() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locatario));

		Locatario result = service.getLocatarioById(0L);

		assertThat(locatario.toString(), hasToString(result.toString()));
	}

}
