package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.service.HouseService;

import java.util.List;

/**
 * @author Vasco Ramos
 * @date 18/mai/2020
 * @time 10:36
 */

@RestController
@RequestMapping("/cities")
public class CitiesController {
	@Autowired
	private HouseService service;

	@GetMapping("")
	public List<String> getCities() {
		return service.getAllCities();
	}
}
