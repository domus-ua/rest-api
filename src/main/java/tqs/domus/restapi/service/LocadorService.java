package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.LocadorRepository;
import tqs.domus.restapi.repository.UserRepository;

import javax.validation.ConstraintViolationException;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 10:24
 */

@Service
public class LocadorService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LocadorRepository repository;

	public Locador registerLocador(UserDTO userDTO) throws ErrorDetails {
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new ErrorDetails("There is already a user with that email");
		} else {
			try {
				User user = new ModelMapper().map(userDTO, User.class);
				userRepository.save(user);
				Locador locador = new Locador();
				locador.setUser(user);
				return repository.save(locador);
			} catch (ConstraintViolationException ex) {
				throw new ErrorDetails("Missing Parameters");
			}
		}
	}

	public Locador getLocadorById(long id) throws ResourceNotFoundException {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Locador not found for this" +
				" " + "id: " + id));
	}
}
