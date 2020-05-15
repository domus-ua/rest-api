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
public class HouseReviewKey implements Serializable {

	@Column(name = "locatario_id")
	private Long locatarioId;

	@Column(name = "house_id")
	private Long houseId;

	/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locadorId == null) ? 0 : locadorId.hashCode());
		result = prime * result + ((locatarioId == null) ? 0 : locatarioId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocatarioReviewKey other = (LocatarioReviewKey) obj;
		if (locadorId == null) {
			if (other.locadorId != null)
				return false;
		} else if (!locadorId.equals(other.locadorId)) {
			return false;
		}
		if (locatarioId == null) {
			return other.locatarioId == null;
		} else return locatarioId.equals(other.locatarioId);
	}
	*/
}
