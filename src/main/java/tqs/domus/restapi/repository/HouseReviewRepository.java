package tqs.domus.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.domus.restapi.model.HouseReview;

import java.util.Optional;

/**
 * @author Vasco Ramos
 * @date 23/mai/2020
 * @time 14:14
 */

public interface HouseReviewRepository extends JpaRepository<HouseReview, Long> {
	Optional<HouseReview> findByHouseIdAndLocatarioId(long houseId, long locatarioId);
}
