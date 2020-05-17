package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.repository.HouseRepository;
import tqs.domus.restapi.repository.LocadorRepository;

import javax.validation.ConstraintViolationException;

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
					.orElseThrow(() -> new ResourceNotFoundException("Locador not found for this id : " + locadorId));
			House house = new ModelMapper().map(houseDTO, House.class);
			house.setLocador(locador);
			return houseRepository.save(house);
		} catch (ConstraintViolationException | NullPointerException ex) {
			throw new ErrorDetails("Missing Parameters");
		}
	}

	public ResponseEntity<?> deleteHouse(long id) throws ResourceNotFoundException {
		House house = houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("House not found " +
				"for this id: " + id));

		houseRepository.delete(house);

		return ResponseEntity.noContent().build();

	}
}
