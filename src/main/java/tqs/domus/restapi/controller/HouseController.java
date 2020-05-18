package tqs.domus.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import java.util.List;

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

	@PutMapping("/{id}")
	public House updateHouse(@PathVariable(value = "id") long id,
							 @Valid @RequestBody HouseDTO houseDTO) throws ResourceNotFoundException {
		return service.updateHouse(id, houseDTO);
	}

	@GetMapping("")
	public List<House> searchHouse(@RequestParam(required = false) String city,
								   @RequestParam(required = false) Integer nRooms,
								   @RequestParam(required = false) Double minPrice,
								   @RequestParam(required = false) Double maxPrice,
								   @RequestParam(required = false) String orderAttribute,
								   @RequestParam(required = false) Boolean desc
	) throws ErrorDetails {

		String orderAtt = "rating";

		boolean order = false;
		if (desc != null) {
			order = desc;
		}

		if (orderAttribute != null) {
			if (!orderAttribute.equals("price") && !orderAttribute.equals("rating")) {
				throw new ErrorDetails("Order attribute not supported");
			} else {
				orderAtt = orderAttribute;
			}
		}

		return service.searchHouse(city, nRooms, minPrice, maxPrice, orderAtt, order);
	}


}
