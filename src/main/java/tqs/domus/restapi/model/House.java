package tqs.domus.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

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
	private int noRooms;

	@NotNull
	private int noBathrooms;

	@NotNull
	private int noGarages;

	@NotNull
	private double habitableArea;

	@NotNull
	private boolean available;

	@NotNull
	private double price;

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

	@ElementCollection
	@Lob
	private List<String> photos;

	@ManyToOne
	@JoinColumn(name = "locador_id", nullable = false)
	private Locador locador;

	@ManyToMany(mappedBy = "wishlist")
	@JsonIgnore
	private Set<Locatario> saves;

	@OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HouseReview> reviewsReceived;

	@OneToOne(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Contract contract;

	@NotNull
	private double averageRating = 0.0;
}
