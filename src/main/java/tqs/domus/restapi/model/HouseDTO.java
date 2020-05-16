package tqs.domus.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Vasco Ramos
 * @date 06/05/20
 * @time 11
 */

@Getter
@AllArgsConstructor
public class HouseDTO {
	private final String street;
	private final String city;
	private final String postalCode;
	private final int nRooms;
	private final int nBathrooms;
	private final int nGarages;
	private final double habitableArea;
	private final boolean availability;
	private final double price;
	private final String name;
	private final String description;
	private final String propertyFeatures;
	private final List<String> photos;
	private final LocadorDTO locador;
}

