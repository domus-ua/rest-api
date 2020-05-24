package tqs.domus.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vasco Ramos
 * @date 15/mai/2020
 * @time 11:47
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HouseReviewDTO {
	private Long houseId;
	private Long locatarioId;
	private String comment;
	private Double rating;
}
