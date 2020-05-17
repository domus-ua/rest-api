package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.service.HouseService;

import javax.validation.Valid;

/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 15:36
 */

@RestController
@RequestMapping("/houses")
public class HouseController {
	@Autowired
	private HouseService service;

	@PostMapping("")
	public House createHouse(@Valid @RequestBody HouseDTO houseDTO) throws ErrorDetails, ResourceNotFoundException {
		return service.registerHouse(houseDTO);
	}

	@GetMapping("/{id}")
	public House searchHouse(@PathVariable(value = "id") Long houseId, @RequestParam(required = false) String city,
							 @RequestParam(required = false) Integer nRooms,
							 @RequestParam(required = false) Double minPrice,
							 @RequestParam(required = false) Double maxPrice,
							 @RequestParam(required = false) String orderAttribute,
							 @RequestParam(required = false) Boolean desc
							 ) throws ErrorDetails, ResourceNotFoundException {
		return null; // TODO: implement this
	}


}
