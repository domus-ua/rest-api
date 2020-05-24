package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.model.WishListDTO;
import tqs.domus.restapi.repository.HouseRepository;
import tqs.domus.restapi.repository.LocatarioRepository;
import tqs.domus.restapi.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 10:24
 */

@Service
public class LocatarioService {
	private static final String LOCATARIO_NOT_FOUND = "Locatário not found for this id: ";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocatarioRepository repository;

	@Autowired
	private HouseRepository houseRepository;

	public Locatario registerLocatario(UserDTO userDTO) throws ErrorDetails {
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new ErrorDetails("There is already a user with that email");
		} else {
			User user;
			Locatario locatario;
			try {
				user = new ModelMapper().map(userDTO, User.class);
				userRepository.save(user);
				locatario = new Locatario();
				locatario.setUser(user);
				return repository.save(locatario);
			} catch (ConstraintViolationException ex) {
				throw new ErrorDetails("Missing Parameters");
			}
		}
	}

	public Locatario getLocatarioById(long id) throws ResourceNotFoundException {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(LOCATARIO_NOT_FOUND + id));
	}

	public ResponseEntity<Void> deleteLocatarioById(long id) throws ResourceNotFoundException {
		Locatario locatario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(LOCATARIO_NOT_FOUND + id));
		userRepository.delete(locatario.getUser());

		return ResponseEntity.noContent().build();

	}

	public Locatario updateLocatarioById(long id, UserDTO userDTO) throws ResourceNotFoundException {
		Locatario locatario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(LOCATARIO_NOT_FOUND + id));

		if (userDTO.getEmail() != null) {
			locatario.getUser().setEmail(userDTO.getEmail());
		}

		if (userDTO.getFirstName() != null) {
			locatario.getUser().setFirstName(userDTO.getFirstName());
		}

		if (userDTO.getLastName() != null) {
			locatario.getUser().setLastName(userDTO.getLastName());
		}

		if (userDTO.getPassword() != null) {
			locatario.getUser().setPassword(userDTO.getPassword());
		}

		if (userDTO.getPhoneNumber() != null) {
			locatario.getUser().setPhoneNumber(userDTO.getPhoneNumber());
		}

		if (userDTO.getSex() != null) {
			locatario.getUser().setSex(userDTO.getSex());
		}

		if (userDTO.getPhoto() != null) {
			locatario.getUser().setPhoto(userDTO.getPhoto());
		}

		return repository.save(locatario);
	}

	public boolean addToWishlist(WishListDTO wishListDTO) throws ResourceNotFoundException {
		Long locatarioId = wishListDTO.getLocatarioId();
		Locatario locatario = repository.findById(locatarioId)
				.orElseThrow(() -> new ResourceNotFoundException(LOCATARIO_NOT_FOUND + locatarioId));

		Long houseId = wishListDTO.getHouseId();
		House house = houseRepository.findById(houseId)
				.orElseThrow(() -> new ResourceNotFoundException("House not found for this id: " + houseId));

		Set<House> wishlist = locatario.getWishlist();
		wishlist.add(house);
		locatario.setWishlist(wishlist);
		repository.save(locatario);

		return true;
	}

	public Set<House> getLocatarioWishlist(long locatarioId) throws ResourceNotFoundException {
		Locatario locatario = repository.findById(locatarioId)
				.orElseThrow(() -> new ResourceNotFoundException(LOCATARIO_NOT_FOUND + locatarioId));

		return locatario.getWishlist();
	}

	public ResponseEntity<Void> deleteFromWishlist(WishListDTO wishListDTO) throws ResourceNotFoundException {
		Long locatarioId = wishListDTO.getLocatarioId();
		Locatario locatario = repository.findById(locatarioId)
				.orElseThrow(() -> new ResourceNotFoundException(LOCATARIO_NOT_FOUND + locatarioId));

		Long houseId = wishListDTO.getHouseId();
		House house = houseRepository.findById(houseId)
				.orElseThrow(() -> new ResourceNotFoundException("House not found for this id: " + locatarioId));

		Set<House> wishlist = locatario.getWishlist();
		wishlist.remove(house);
		locatario.setWishlist(wishlist);
		repository.save(locatario);

		return ResponseEntity.noContent().build();
	}
}
