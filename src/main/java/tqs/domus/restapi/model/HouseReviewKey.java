package tqs.domus.restapi.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Vasco Ramos
 * @date 15/mai/2020
 * @time 11:39
 */

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HouseReviewKey implements Serializable {
	@Column(name = "locatario_id")
	private Long locatarioId;

	@Column(name = "house_id")
	private Long houseId;

}
