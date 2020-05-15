package tqs.domus.restapi.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
 * @time 11:47
 */

@Entity
@Setter
@Getter
public class LocatarioReview {
	@EmbeddedId
	private LocatarioReviewKey id;

	@ManyToOne
	@MapsId("locatario_id")
	@JoinColumn(name = "locatario_id")
	private Locatario locatario;

	@ManyToOne
	@MapsId("locador_id")
	@JoinColumn(name = "locador_id")
	private Locador locador;

	@NotNull
	private Double rating;

	@NotNull
	private String comment;

	@CreationTimestamp
	private Timestamp timestamp;
}
