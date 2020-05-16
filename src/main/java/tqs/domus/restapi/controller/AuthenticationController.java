package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.service.UserService;

import java.util.Base64;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 16:54
 */

@RestController
@RequestMapping("/users")
public class AuthenticationController {

	@Autowired
	private UserService service;

	@PostMapping("/login")
	public Object login(@RequestHeader("Authorization") String auth) throws ErrorDetails, ResourceNotFoundException {
		String base64Credentials = auth.substring("Basic".length()).trim();
		String[] credentials = new String(Base64.getDecoder().decode(base64Credentials)).split(":", 2);
		String email = credentials[0];
		String password = credentials[1];
		return service.login(email, password);
	}

	@GetMapping("/logout")
	public String logout() {
		return "Logout Successful!";
	}
}
