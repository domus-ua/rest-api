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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Vasco Ramos
 * @date 06/05/20
 * @time 11
 */

@Entity
@Setter
@Getter
public class User {
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
	private String sex;

	@NotNull
	private String role; // locador or locatário

	@Lob
	private String photo;

	@CreationTimestamp
	@Setter(AccessLevel.NONE)
	private Timestamp dateJoined;

	private Timestamp lastLogin;

	@ManyToMany
	@JoinTable(name = "wishlist",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "house_id"))
	@JsonIgnore
	private List<House> wishlist;

	@NotNull
	private boolean isVerified = false;

	// TODO: missing rentals and reviews attributes

	public boolean isLocador() {
		return role.equals("locador");
	}

	public boolean isLocatario() {
		return role.equals("locatário");
	}
}

