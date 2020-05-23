package tqs.domus.restapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.exception.ErrorDetails;
import tqs.domus.restapi.exception.ResourceNotFoundException;
import tqs.domus.restapi.model.House;
import tqs.domus.restapi.model.HouseDTO;
import tqs.domus.restapi.model.HouseReview;
import tqs.domus.restapi.model.HouseReviewDTO;
import tqs.domus.restapi.model.HouseReviewKey;
import tqs.domus.restapi.model.Locador;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.repository.HouseRepository;
import tqs.domus.restapi.repository.HouseReviewRepository;
import tqs.domus.restapi.repository.LocadorRepository;
import tqs.domus.restapi.repository.LocatarioRepository;

import javax.validation.ConstraintViolationException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 15:38
 */

@Service
public class HouseService {
	private static final String HOUSE_NOT_FOUND = "House not found for this id: ";
	private static final String REVIEW_NOT_FOUND = "Review not found";

	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private HouseReviewRepository houseReviewRepository;

	@Autowired
	private LocadorRepository locadorRepository;

	@Autowired
	private LocatarioRepository locatarioRepository;

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
		House house =
				houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOUSE_NOT_FOUND + id));

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
			house.setNoBathrooms(houseDTO.getNoBathrooms());
		}

		if (houseDTO.getNoGarages() != 0) {
			house.setNoGarages(houseDTO.getNoGarages());
		}

		if (houseDTO.getNoRooms() != 0) {
			house.setNoRooms(houseDTO.getNoRooms());
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

	public House getHouse(long id) throws ResourceNotFoundException {
		return houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOUSE_NOT_FOUND + id));
	}

	public ResponseEntity<Void> deleteHouse(long id) throws ResourceNotFoundException {
		House house =
				houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOUSE_NOT_FOUND + id));

		houseRepository.delete(house);

		return ResponseEntity.noContent().build();
	}

	public HouseReview registerReview(HouseReviewDTO houseReviewDTO) throws ErrorDetails, ResourceNotFoundException {
		try {
			long locatarioId = houseReviewDTO.getLocatarioId();
			Locatario locatario = locatarioRepository.findById(locatarioId)
					.orElseThrow(() -> new ResourceNotFoundException("Locatario not found for this id: " + locatarioId));

			long houseId = houseReviewDTO.getHouseId();
			House house = houseRepository.findById(houseId)
					.orElseThrow(() -> new ResourceNotFoundException(HOUSE_NOT_FOUND + houseId));

			HouseReviewKey key = new HouseReviewKey(locatarioId, houseId);

			HouseReview houseReview = new HouseReview();
			houseReview.setId(key);
			houseReview.setComment(houseReviewDTO.getComment());
			houseReview.setRating(houseReviewDTO.getRating());
			houseReview.setHouse(house);
			houseReview.setLocatario(locatario);
			HouseReview review = houseReviewRepository.save(houseReview);

			house.setAverageRating(calculateAvgRating(house.getReviewsReceived()));
			houseRepository.save(house);

			return review;
		} catch (ConstraintViolationException ex) {
			throw new ErrorDetails("Missing Parameters");
		}
	}

	public List<HouseReview> getHouseReviews(long id) throws ResourceNotFoundException {
		House house =
				houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HOUSE_NOT_FOUND + id));
		return house.getReviewsReceived();
	}

	public HouseReview getHouseReview(long houseId, long locatarioId) throws ResourceNotFoundException {
		return houseReviewRepository.findByHouseIdAndLocatarioId(houseId, locatarioId).orElseThrow(()
				-> new ResourceNotFoundException(REVIEW_NOT_FOUND));
	}

	public HouseReview updateHouseReview(HouseReviewDTO houseReviewDTO) throws ResourceNotFoundException {
		long locatarioId = houseReviewDTO.getLocatarioId();
		long houseId = houseReviewDTO.getHouseId();

		HouseReview review = houseReviewRepository.findByHouseIdAndLocatarioId(houseId, locatarioId).orElseThrow(()
				-> new ResourceNotFoundException(REVIEW_NOT_FOUND));
		House house = review.getHouse();

		if (houseReviewDTO.getComment() != null) {
			review.setComment(houseReviewDTO.getComment());
		}

		if (houseReviewDTO.getRating() != null) {
			review.setRating(houseReviewDTO.getRating());
		}
		review.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
		houseReviewRepository.save(review);

		house.setAverageRating(calculateAvgRating(house.getReviewsReceived()));
		houseRepository.save(house);

		return review;

	}

	public ResponseEntity<Void> deleteHouseReview(long houseId, long locatarioId) throws ResourceNotFoundException {
		HouseReview review = houseReviewRepository.findByHouseIdAndLocatarioId(houseId, locatarioId).orElseThrow(()
				-> new ResourceNotFoundException(REVIEW_NOT_FOUND));

		House house = review.getHouse();

		houseReviewRepository.delete(review);

		house.setAverageRating(calculateAvgRating(house.getReviewsReceived()));
		houseRepository.save(house);

		return ResponseEntity.noContent().build();
	}

	private double calculateAvgRating(List<HouseReview> reviews) {
		if (reviews == null || reviews.isEmpty()) {
			return 0D;
		}
		double totalRating = reviews.stream().mapToDouble(HouseReview::getRating).sum();
		return totalRating / reviews.size();
	}
}
