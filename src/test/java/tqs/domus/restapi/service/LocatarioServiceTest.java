package tqs.domus.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.model.WishListDTO;
import tqs.domus.restapi.repository.HouseRepository;
import tqs.domus.restapi.repository.LocatarioRepository;
import tqs.domus.restapi.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

	@Mock(lenient = true)
	private HouseRepository houseRepository;

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
	void testRegisterLocatario_missingParameters() {
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

	@Test
	void testDeleteLocatarioById_existentId() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locatario));

		ResponseEntity<?> result = service.deleteLocatarioById(0L);

		assertThat(ResponseEntity.noContent().build(), hasToString(result.toString()));
	}

	@Test
	void testUpdateLocatarioById_inexistentId() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);


		assertThrows(ResourceNotFoundException.class, () -> {
			service.updateLocatarioById(0L, userDTO);
		});
	}

	@Test
	void testUpdateLocatario_completeUpdate() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);
		UserDTO updatedUserDTO = new UserDTO("v@ua.pt", "João", "Vasconcelos", "pwd", "123", "M", null);

		User updatedUser = new ModelMapper().map(updatedUserDTO, User.class);
		Locatario updatedLocatario = new Locatario();
		updatedLocatario.setUser(updatedUser);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locatario));
		when(repository.save(any(Locatario.class))).thenReturn(updatedLocatario);

		Locatario result = service.updateLocatarioById(1L, userDTO);
		assertThat(updatedLocatario.toString(), hasToString(result.toString()));

	}

	@Test
	void testDeleteLocatarioById_inexistentId() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteLocatarioById(0L);
		});
	}

	@Test
	void testUpdateLocatario_partialUpdate() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		UserDTO updatedUserDTO = new UserDTO(null, null, null, null, null, null, "photo1");

		User updatedUser = new ModelMapper().map(updatedUserDTO, User.class);
		Locatario updatedLocatario = new Locatario();
		updatedLocatario.setUser(updatedUser);

		when(repository.findById(anyLong())).thenReturn(Optional.of(locatario));
		when(repository.save(any(Locatario.class))).thenReturn(updatedLocatario);

		Locatario result = service.updateLocatarioById(1L, userDTO);
		assertThat(updatedLocatario.toString(), hasToString(result.toString()));
	}

	@Test
	void testAddToWishList_locatarioDoesNotExist() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.addToWishlist(new WishListDTO());
		});
	}

	@Test
	void testAddToWishList_houseDoesNotExist() {
		when(houseRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.addToWishlist(new WishListDTO());
		});
	}

	@Test
	void testGetWishList_locatarioDoesNotExist() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.getLocatarioById(1L);
		});
	}

	@Test
	void testGetWishList_locatarioExists() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, null);
		House house = new ModelMapper().map(houseDTO, House.class);

		Set<House> wishlist = new HashSet<>() {{
			add(house);
		}};
		locatario.setWishlist(wishlist);
		when(repository.findById(anyLong())).thenReturn(Optional.of(locatario));

		Set<House> result = service.getLocatarioWishlist(locatario.getId());
		assertThat(wishlist.toString(), hasToString(result.toString()));
	}

	@Test
	void testDeleteHouseFromWishList_locatarioDoesNotExist() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteFromWishlist(new WishListDTO());
		});
	}

	@Test
	void testDeleteHouseFromWishList_houseDoesNotExist() {
		when(houseRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteFromWishlist(new WishListDTO());
		});
	}

	@Test
	void testDeleteHouseFromWishList_locatarioAndHouseExist() throws ResourceNotFoundException {
		UserDTO userDTO = new UserDTO("v@ua.pt", "Vasco", "Ramos", "pwd", "123", "M", null);

		User user = new ModelMapper().map(userDTO, User.class);
		Locatario locatario = new Locatario();
		locatario.setUser(user);

		HouseDTO houseDTO = new HouseDTO("Av. da Misericórdia", "São João da Madeira", "3700-191", 2, 2, 2, 300, true
				, 230, "Casa T2", "Casa muito bonita", "WI-FI;Máquina de lavar", null, null);
		House house = new ModelMapper().map(houseDTO, House.class);
		locatario.setWishlist(new HashSet<>() {{
			add(house);
		}});

		when(repository.findById(anyLong())).thenReturn(Optional.of(locatario));
		when(houseRepository.findById(anyLong())).thenReturn(Optional.of(house));

		WishListDTO wishListDTO = new WishListDTO(locatario.getId(), house.getId());

		ResponseEntity<?> result = service.deleteFromWishlist(wishListDTO);

		assertThat(ResponseEntity.noContent().build(), hasToString(result.toString()));
	}

}
