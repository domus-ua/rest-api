package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.repository.HouseRepository;
import tqs.domus.restapi.repository.LocadorRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 15:38
 */

@Service
public class HouseService {
	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private LocadorRepository locadorRepository;

	public House registerHouse(HouseDTO houseDTO) throws ErrorDetails, ResourceNotFoundException {
		try {
			long locadorId = houseDTO.getLocador().getId();
			Locador locador = locadorRepository.findById(locadorId)
					.orElseThrow(() -> new ResourceNotFoundException("Locador not found for this id: " + locadorId));
			House house = new ModelMapper().map(houseDTO, House.class);
			house.setLocador(locador);
			return houseRepository.save(house);
		} catch (ConstraintViolationException | NullPointerException ex) {
			throw new ErrorDetails("Missing Parameters");
		}
	}


	public List<House> searchHouse(String city, Integer nRooms, Double minPrice, Double maxPrice,
								   String orderAttribute, boolean desc) {
		if (orderAttribute.equals("price") && desc) {
			return houseRepository.findByAttributesDescPrice(city, nRooms, minPrice, maxPrice);
		} else if (orderAttribute.equals("price")) {
			return houseRepository.findByAttributesAscPrice(city, nRooms, minPrice, maxPrice);
		} else if (orderAttribute.equals("rating") && !desc) {
			return houseRepository.findByAttributesAscRating(city, nRooms, minPrice, maxPrice);
		} else {
			return houseRepository.findByAttributesDescRating(city, nRooms, minPrice, maxPrice);
		}
	}


	public House updateHouse(long id, HouseDTO houseDTO) throws ResourceNotFoundException {
		House house = houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("House not found " +
				"for this id: " + id));

		if (houseDTO.getCity() != null) {
			house.setCity(houseDTO.getCity());
		}

		if (houseDTO.getDescription() != null) {
			house.setDescription(houseDTO.getDescription());
		}

		if (houseDTO.getHabitableArea() != 0D) {
			house.setHabitableArea(houseDTO.getHabitableArea());
		}

		if (houseDTO.getName() != null) {
			house.setName(houseDTO.getName());
		}

		if (houseDTO.getNoBathrooms() != 0) {
			house.setNBathrooms(houseDTO.getNoBathrooms());
		}

		if (houseDTO.getNoGarages() != 0) {
			house.setNGarages(houseDTO.getNoGarages());
		}

		if (houseDTO.getNoRooms() != 0) {
			house.setNRooms(houseDTO.getNoRooms());
		}

		if (houseDTO.getPhotos() != null) {
			house.setPhotos(houseDTO.getPhotos());
		}

		if (houseDTO.getPostalCode() != null) {
			house.setPostalCode(houseDTO.getPostalCode());
		}

		if (houseDTO.getPrice() != 0D) {
			house.setPrice(houseDTO.getPrice());
		}

		if (houseDTO.getPropertyFeatures() != null) {
			house.setPropertyFeatures(houseDTO.getPropertyFeatures());
		}

		if (houseDTO.getStreet() != null) {
			house.setStreet(houseDTO.getStreet());
		}

		return houseRepository.save(house);
	}

	public List<String> getAllCities() {
		return houseRepository.findAllCities();
	}
}
