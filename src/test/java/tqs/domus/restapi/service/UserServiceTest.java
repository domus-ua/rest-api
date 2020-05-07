package tqs.domus.restapi.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;

import java.sql.Timestamp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 22:01
 */

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock(lenient = true)
	private UserRepository repository;

	@InjectMocks
	private UserService service;

	@BeforeEach
	void setUp() {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");
		User user = new ModelMapper().map(userDTO, User.class);
		when(repository.findByEmail(anyString())).thenAnswer(invocation -> {
			Object arg = invocation.getArgument(0);
			if (user.getEmail().equals(arg)) return user;
			else return null;
		});
	}

	@Test
	void testRegisterUser_emailIsTaken() {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");
		when(repository.existsByEmail(anyString())).thenReturn(true);
		assertThrows(ErrorDetails.class, () -> {
			service.registerUser(userDTO);
		});

	}

	@Test
	void testRegisterUser_emailIsFree() throws ErrorDetails {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");
		when(repository.existsByEmail(anyString())).thenReturn(false);

		User user = new ModelMapper().map(userDTO, User.class);
		when(repository.save(any(User.class))).thenReturn(user);

		User result = service.registerUser(userDTO);
		assertThat(user.toString(), hasToString(result.toString()));
	}

	@Test
	void testGetUserByEmail_existentUser() {
		String email = "v@ua.pt";
		User found = service.getUserByEmail(email);
		assertThat(email, equalTo(found.getEmail()));
	}

	@Test
	void testGetUserByEmail_nonExistentUser() {
		User found = service.getUserByEmail("non@existent.pt");
		Assertions.assertThat(found).isNull();
	}

	@Test
	void testLogin_ExistentEmail() throws ErrorDetails {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");
		User user = new ModelMapper().map(userDTO, User.class);
		String timestamp = "2011-10-02 18:48:05.123";
		user.setLastLogin(Timestamp.valueOf(timestamp));
		when(repository.existsByEmail(anyString())).thenReturn(true);
		when(repository.save(any(User.class))).thenReturn(user);

		User result = service.login("v@ua.pt", "pwd");
		assertThat(result.getLastLogin(), is(notNullValue()));
		assertThat(result.getLastLogin().toString(), hasToString(timestamp));
	}

	@Test
	void testLogin_InexistentEmail() {
		when(repository.existsByEmail(anyString())).thenReturn(false);
		assertThrows(ErrorDetails.class, () -> {
			service.login("v@ua.pt", "pwd");
		});
	}

	@Test
	void testLogin_InvalidPassword() {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");
		User user = new ModelMapper().map(userDTO, User.class);
		when(repository.existsByEmail(anyString())).thenReturn(true);
		when(repository.save(any(User.class))).thenReturn(user);

		assertThrows(ErrorDetails.class, () -> {
			service.login("v@ua.pt", "pws");
		});
	}
}
