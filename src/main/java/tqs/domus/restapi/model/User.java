package tqs.domus.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Vasco Ramos
 * @date 06/05/20
 * @time 11
 */

@Entity
@Setter
@Getter
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private long id;

	@NotNull
	private String email;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@JsonIgnore
	@NotNull
	private String password;

	@NotNull
	private String phoneNumber;

	@NotNull
	private String role; // locador or locatário

	@CreationTimestamp
	@Setter(AccessLevel.NONE)
	private Timestamp dateJoined;

	private Timestamp lastLogin;
}

