package tqs.domus.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author Vasco Ramos
 * @date 12/mai/2020
 * @time 21:47
 */

@Entity
@Setter
@Getter
public class HousePhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private long id;

	@Lob
	@NotNull
	private String photo;

	@ManyToOne
	@JoinColumn(name = "house_id")
	@JsonIgnore
	private House house;

}
