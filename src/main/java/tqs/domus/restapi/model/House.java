package tqs.domus.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Vasco Ramos
 * @date 12/mai/2020
 * @time 21:44
 */

@Entity
@Setter
@Getter
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private long id;

	@NotNull
	private String street;

	@NotNull
	private String city;

	@NotNull
	private String postalCode;

	@NotNull
	private int nRooms;

	@NotNull
	private int nBathrooms;

	@NotNull
	private int nGarages;

	@NotNull
	private float habitableArea;

	@NotNull
	private int availability;

	@NotNull
	private float price;

	@NotNull
	private String name;

	@Lob
	@NotNull
	private String description;

	@NotNull
	private String propertyFeatures;

	@CreationTimestamp
	@Setter(AccessLevel.NONE)
	private Timestamp publishDay;

	@OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<HousePhoto> photos;

	@ManyToMany(mappedBy = "wishlist")
	@JsonIgnore
	private List<Locatario> saves;

	@OneToMany(mappedBy = "house")
	private List<HouseReview> reviewsReceived;

	@OneToOne(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	@NotNull
	private Contract contract;

}
