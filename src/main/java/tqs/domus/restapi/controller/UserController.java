package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserDTO;
import tqs.domus.restapi.service.UserService;

import javax.validation.Valid;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 13:52
 */

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping("")
	public User createUser(@Valid @RequestBody UserDTO userDTO) throws ErrorDetails {
		return service.registerUser(userDTO);
	}

}
