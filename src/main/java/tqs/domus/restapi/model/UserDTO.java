package tqs.domus.restapi.model;

import lombok.Data;

/**
 * @author Vasco Ramos
 * @date 06/05/20
 * @time 11
 */

@Data
public class UserDTO {
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String phoneNumber;
	private String role; // locador or locatário

	public UserDTO() {
	}

	public UserDTO(String email, String firstName, String lastName, String password, String phoneNumber, String role) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}
}

