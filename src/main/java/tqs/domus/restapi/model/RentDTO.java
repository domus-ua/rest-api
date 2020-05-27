package tqs.domus.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Vasco Ramos
 * @date 25/mai/2020
 * @time 15:20
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentDTO {
	private Long houseId;
	private String locatarioEmail;
	private Long locadorId;
	private Timestamp startDate;
	private Timestamp endDate;
	private double price;
}
