package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;

import java.util.Calendar;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 14:00
 */

@Service
public class UserService {
	@Autowired
	private UserRepository repository;

	public User registerUser(UserDTO userDTO) throws ErrorDetails {
		if (repository.existsByEmail(userDTO.getEmail())) {
			throw new ErrorDetails("There is already a user with that email");
		}

		String encodedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);
		User user = new ModelMapper().map(userDTO, User.class);
		return repository.save(user);
	}

	public User getUserByEmail(String email) {
		User user = repository.findByEmail(email);
		user.setLastLogin(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		return repository.save(user);
	}
}
