package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.model.WishListDTO;
import tqs.domus.restapi.service.LocatarioService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 10:23
 */

@RestController
@RequestMapping("/locatarios")
public class LocatarioController {
	@Autowired
	private LocatarioService service;

	@PostMapping("")
	public Locatario createUser(@Valid @RequestBody UserDTO userDTO) throws ErrorDetails {
		return service.registerLocatario(userDTO);
	}

	@GetMapping("/{id}")
	public Locatario getUserById(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
		return service.getLocatarioById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLocatarioById(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
		return service.deleteLocatarioById(id);
	}

	@PutMapping("/{id}")
	public Locatario updateLocatarioById(@PathVariable(value = "id") long id,
										 @Valid @RequestBody UserDTO userDTO) throws ResourceNotFoundException {
		return service.updateLocatarioById(id, userDTO);
	}

	@PostMapping("/wishlist")
	public String addHouseToWishList(@Valid @RequestBody WishListDTO wishListDTO) throws ResourceNotFoundException {
		service.addToWishlist(wishListDTO);
		return "House added successfully to wishlist";
	}

	@GetMapping("/wishlist/{id}")
	public List<House> getLocatarioWishList(@PathVariable(value = "id") long locatarioId)
			throws ResourceNotFoundException {
		return service.getLocatarioWishlist(locatarioId);
	}

}
