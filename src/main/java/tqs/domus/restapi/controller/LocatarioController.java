package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.LocatarioService;

import javax.validation.Valid;

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
}
