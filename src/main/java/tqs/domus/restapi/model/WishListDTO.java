package tqs.domus.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Vasco Ramos
 * @date 23/mai/2020
 * @time 20:15
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WishListDTO {
	private Long locatarioId;
	private Long houseId;
}
