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

	public House registerHouse(HouseDTO houseDTO) throws ErrorDetails,ResourceNotFoundException {
		try {
			long locadorId = houseDTO.getLocador().getId();
			Locador locador = locadorRepository.findById(locadorId)
					.orElseThrow(() -> new ResourceNotFoundException("Locador not found for this id : " + locadorId));
			House house = new ModelMapper().map(houseDTO, House.class);
			house.setLocador(locador);
			return houseRepository.save(house);
		} catch (ConstraintViolationException | NullPointerException ex) {
			throw new ErrorDetails("Missing Parameters");
		}
	}


	public List<House> searchHouse(String city, Integer nRooms, Double minPrice, Double maxPrice, String orderAttribute, Boolean desc) {
		if(orderAttribute.equals("price") && desc){
			return houseRepository.findByAttributesDesc(city, nRooms, minPrice, maxPrice);
		}
		else if(orderAttribute.equals("price")){
			return houseRepository.findByAttributesAsc(city, nRooms, minPrice, maxPrice);
		}
		// TODO: sort by ratings
		else{
			return null;
		}
	}


}
