package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.LocadorRepository;
import tqs.domus.restapi.repository.LocatarioRepository;
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

	@Autowired
	private LocadorRepository locadorRepository;

	@Autowired
	private LocatarioRepository locatarioRepository;

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

	public Object login(String email, String password) throws ErrorDetails, ResourceNotFoundException {
		if (repository.existsByEmail(email)) {
			User user = getUserByEmail(email);
			if (user.getPassword().equals(password)) {
				user.setLastLogin(new Timestamp(Calendar.getInstance().getTime().getTime()));
				repository.save(user);

				if (user.getLocador() != null) {
					return user.getLocador();
				} else if (user.getLocatario() != null) {
					return user.getLocatario();
				} else {
					throw new ResourceNotFoundException("User registry seems to have encounter a problem!");
				}

			} else {
				throw new ErrorDetails("Invalid Credentials");
			}
		} else {
			throw new ErrorDetails("Invalid Credentials");
		}
	}
}
