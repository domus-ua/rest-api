package tqs.domus.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Vasco Ramos
 * @date 06/05/20
 * @time 11
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HouseDTO {
	private String street;
	private String city;
	private String postalCode;
	private int noRooms;
	private int noBathrooms;
	private int noGarages;
	private double habitableArea;
	private boolean available;
	private double price;
	private String name;
	private String description;
	private String propertyFeatures;
	private List<String> photos;
	private LocadorDTO locador;
}
