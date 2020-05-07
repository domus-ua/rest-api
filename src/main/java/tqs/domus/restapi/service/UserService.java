package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.UserRepository;

import java.sql.Timestamp;
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
		} else {
			User user = new ModelMapper().map(userDTO, User.class);
			return repository.save(user);
		}
	}

	public User getUserByEmail(String email) {
		return repository.findByEmail(email);
	}

	public User login(String email, String password) throws ErrorDetails {
		if (repository.existsByEmail(email)) {
			User user = getUserByEmail(email);
			if (user.getPassword().equals(password)) {

				user.setLastLogin(new Timestamp(Calendar.getInstance().getTime().getTime()));
				return repository.save(user);
			} else {
				throw new ErrorDetails("Invalid Credentials");
			}
		} else {
			throw new ErrorDetails("Invalid Credentials");
		}
	}
}
