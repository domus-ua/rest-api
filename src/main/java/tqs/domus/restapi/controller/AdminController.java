package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.service.AdminService;

/**
 * @author Vasco Ramos
 * @date 26/mai/2020
 * @time 10:49
 */

@RestController
public class AdminController {

	@Autowired
	private AdminService service;

	@GetMapping("/reload-db")
	public String logout() {
		boolean retVal = service.reloadDatabse();
		if (retVal) {
			return "Reload db completed successfully!";
		} else {
			return "Something wrong happened while reloading db!";
		}
	}
}
