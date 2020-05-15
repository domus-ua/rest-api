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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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


	@OneToOne(mappedBy = "locador", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private User user;

	@NotNull
	private boolean isVerified = false;

	@OneToMany(mappedBy = "locador")
	private List<LocatarioReview> reviews;
}
