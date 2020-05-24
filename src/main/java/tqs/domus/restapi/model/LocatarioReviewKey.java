package tqs.domus.restapi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Vasco Ramos
 * @date 15/mai/2020
 * @time 11:39
 */

@Embeddable
@Getter
@EqualsAndHashCode
public class LocatarioReviewKey implements Serializable {
	@Column(name = "locatario_id")
	private Long locatarioId;

	@Column(name = "locador_id")
	private Long locadorId;
}
