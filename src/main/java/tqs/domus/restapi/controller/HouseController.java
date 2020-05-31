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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.HouseReview;
import tqs.domus.restapi.model.HouseReviewDTO;
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
	public House updateHouse(@PathVariable(value = "id") long id, @Valid @RequestBody HouseDTO houseDTO)
			throws ResourceNotFoundException {
		return service.updateHouse(id, houseDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteHouse(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
		return service.deleteHouse(id);
	}

	@GetMapping("")
	public List<House> searchHouse(@RequestParam(required = false) String city,
								   @RequestParam(required = false) Integer nRooms, @RequestParam(required = false) Double minPrice,
								   @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String orderAttribute,
								   @RequestParam(required = false) Boolean desc) throws ErrorDetails {

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

	@GetMapping("/trending")
	public List<House> getTrendingHouses() {
		return service.getTrendingHouses();
	}

	@GetMapping("/{id}")
	public House getHouse(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
		return service.getHouse(id);
	}

	@PostMapping("/reviews")
	public HouseReview createHouseReview(@Valid @RequestBody HouseReviewDTO houseReviewDTO)
			throws ErrorDetails, ResourceNotFoundException {
		return service.registerReview(houseReviewDTO);
	}

	@GetMapping("/reviews/{houseId}")
	public List<HouseReview> getHouseReviews(@PathVariable(value = "houseId") long houseId)
			throws ResourceNotFoundException {
		return service.getHouseReviews(houseId);
	}

	@GetMapping("/reviews/{houseId}/{locatarioId}")
	public HouseReview getHouseReview(@PathVariable(value = "houseId") long houseId,
									  @PathVariable(value = "locatarioId") long locatarioId) throws ResourceNotFoundException {
		return service.getHouseReview(houseId, locatarioId);
	}

	@PutMapping("/reviews/")
	public HouseReview updateHouseReview(@Valid @RequestBody HouseReviewDTO houseReviewDTO)
			throws ResourceNotFoundException {
		return service.updateHouseReview(houseReviewDTO);
	}

	@DeleteMapping("/reviews/{houseId}/{locatarioId}")
	public ResponseEntity<Void> deleteHouseReview(@PathVariable(value = "houseId") long houseId,
												  @PathVariable(value = "locatarioId") long locatarioId) throws ResourceNotFoundException {
		return service.deleteHouseReview(houseId, locatarioId);
	}

}
