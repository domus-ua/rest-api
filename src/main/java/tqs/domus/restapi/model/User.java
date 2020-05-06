package tqs.domus.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
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
@Data
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private Timestamp dateJoined;

	@NotNull
	private int isActive = 1; // default value to 1 ("active")

	private Timestamp lastLogin;

	public User() {
	}

	public User(long id, String email, String firstName, String lastName, String password, String phoneNumber,
				String role, Timestamp lastLogin) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.lastLogin = lastLogin;
	}
}

