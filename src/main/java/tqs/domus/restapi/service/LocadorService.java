package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.repository.LocadorRepository;
import tqs.domus.restapi.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 10:24
 */

@Service
public class LocadorService {
	private static final String LOCADOR_NOT_FOUND = "Locador not found for this id: ";
	private static final int QUALITY_THRESHOLD = 4;

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
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(LOCADOR_NOT_FOUND + id));
	}

	public ResponseEntity<Void> deleteLocadorById(long id) throws ResourceNotFoundException {
		Locador locador = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(LOCADOR_NOT_FOUND + id));

		userRepository.delete(locador.getUser());

		return ResponseEntity.noContent().build();

	}

	public Locador updateLocadorById(long id, UserDTO userDTO) throws ResourceNotFoundException {
		Locador locador = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(LOCADOR_NOT_FOUND + id));

		if (userDTO.getEmail() != null) {
			locador.getUser().setEmail(userDTO.getEmail());
		}

		if (userDTO.getFirstName() != null) {
			locador.getUser().setFirstName(userDTO.getFirstName());
		}

		if (userDTO.getLastName() != null) {
			locador.getUser().setLastName(userDTO.getLastName());
		}

		if (userDTO.getPassword() != null) {
			locador.getUser().setPassword(userDTO.getPassword());
		}

		if (userDTO.getPhoneNumber() != null) {
			locador.getUser().setPhoneNumber(userDTO.getPhoneNumber());
		}

		if (userDTO.getSex() != null) {
			locador.getUser().setSex(userDTO.getSex());
		}

		if (userDTO.getPhoto() != null) {
			locador.getUser().setPhoto(userDTO.getPhoto());
		}

		return repository.save(locador);

	}

	public List<House> getLocadorHouses(long locadorId) throws ResourceNotFoundException {
		Locador locador = repository.findById(locadorId).orElseThrow(
				() -> new ResourceNotFoundException(LOCADOR_NOT_FOUND + locadorId));

		return locador.getHouses();
	}

	public String checkQualityParameter(long locadorId) throws ResourceNotFoundException {
		Locador locador = repository.findById(locadorId).orElseThrow(
				() -> new ResourceNotFoundException(LOCADOR_NOT_FOUND + locadorId));

		List<House> locadorHouses = locador.getHouses();
		double avgRating = calculateAvgRating(locadorHouses);

		boolean isVerified = avgRating >= QUALITY_THRESHOLD;

		locador.setVerified(isVerified);
		repository.save(locador);

		if (isVerified) {
			return "Your request was accepted! Your quality is now verified!";
		} else {
			return "Your request was denied!";
		}
	}

	private double calculateAvgRating(List<House> houses) {
		if (houses == null || houses.isEmpty()) {
			return 0D;
		}
		double totalRating = houses.stream().mapToDouble(House::getAverageRating).sum();
		return totalRating / houses.size();
	}
}
