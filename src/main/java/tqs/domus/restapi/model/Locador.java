package tqs.domus.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Vasco Ramos
 * @date 15/mai/2020
 * @time 11:25
 */

@Entity
@Setter
@Getter
public class Locador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	@OneToMany(mappedBy="locador", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<House> houses;

	@OneToMany(mappedBy = "locador", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Contract> contracts;

	@NotNull
	private boolean isVerified = false;

	@NotNull
	@Setter(AccessLevel.NONE)
	private String role = "locador";

	@OneToMany(mappedBy = "locador")
	private List<LocatarioReview> reviews;
}
