package tqs.domus.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
	@EmbeddedId
	private ContractKey id;

	@ManyToOne
	@MapsId("locatario_id")
	@JoinColumn(name = "locatario_id")
	private Locatario locatario;

	@ManyToOne
	@MapsId("house_id")
	@JoinColumn(name = "house_id")
	@JsonIgnore
	private House house;

	@NotNull
	private Timestamp startDate;

	@NotNull
	private Timestamp endDate;

	@NotNull
	private double price;
}
