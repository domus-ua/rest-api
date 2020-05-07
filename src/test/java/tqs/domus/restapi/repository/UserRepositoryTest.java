package tqs.domus.restapi.repository;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 21:10
 */

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Autowired
	private TestEntityManager manager;

	@Test
	void testFindByEmail_existentEmail() {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");
		User user = new ModelMapper().map(userDTO, User.class);
		manager.persistAndFlush(user);

		User found = repository.findByEmail(user.getEmail());
		assertThat(user.toString(), hasToString(found.toString()));
	}

	@Test
	void testFindByEmail_nonExistentEmail() {
		User found = repository.findByEmail("non@existent.com");
		assertThat(found, is(nullValue()));
	}

	@Test
	void testExistsByEmail_existentEmail() {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "locador");
		User user = new ModelMapper().map(userDTO, User.class);
		manager.persistAndFlush(user);

		Boolean found = repository.existsByEmail(user.getEmail());
		assertThat(found, is(true));
	}

	@Test
	void testExistsByEmail_nonExistentEmail() {
		Boolean found = repository.existsByEmail("non@existent.com");
		assertThat(found, is(false));
	}
}
