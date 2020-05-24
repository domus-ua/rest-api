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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author Vasco Ramos
 * @date 15/mai/2020
 * @time 11:25
 */

@Entity
@Setter
@Getter
public class Contract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private long id;

	@OneToOne
	@JoinColumn(name = "locatario_id")
	@NotNull
	private Locatario locatario;

	@ManyToOne
	@JoinColumn(name = "locador_id")
	@JsonIgnore
	@NotNull
	private Locador locador;

	@OneToOne
	@JoinColumn(name = "house_id")
	@NotNull
	private House house;

	@NotNull
	private Timestamp startDate;

	@NotNull
	private Timestamp endDate;

}
