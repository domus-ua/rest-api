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
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.LocadorService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 10:23
 */

@RestController
@RequestMapping("/locadores")
public class LocadorController {
	@Autowired
	private LocadorService service;

	@PostMapping("")
	public Locador createLocador(@Valid @RequestBody UserDTO userDTO) throws ErrorDetails {
		return service.registerLocador(userDTO);
	}

	@GetMapping("/{id}")
	public Locador getLocadorById(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
		return service.getLocadorById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLocadorById(@PathVariable(value = "id") long id)
			throws ResourceNotFoundException {
		return service.deleteLocadorById(id);
	}

	@PutMapping("/{id}")
	public Locador updateLocadorById(@PathVariable(value = "id") long id, @Valid @RequestBody UserDTO userDTO)
			throws ResourceNotFoundException {
		return service.updateLocadorById(id, userDTO);
	}

	@GetMapping("/houses/{id}")
	public List<House> getLocadorHouses(@PathVariable(value = "id") long locadorId) throws ResourceNotFoundException {
		return service.getLocadorHouses(locadorId);
	}

	@GetMapping("/check-quality/{id}")
	public String checkQualityParameter(@PathVariable(value = "id") long locadorId) throws ResourceNotFoundException {
		return service.checkQualityParameter(locadorId);
	}

}
