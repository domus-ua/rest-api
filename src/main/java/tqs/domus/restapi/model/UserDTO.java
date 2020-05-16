package tqs.domus.restapi.model;

import lombok.Getter;

/**
 * @author Vasco Ramos
 * @date 06/05/20
 * @time 11
 */

@Getter
public class UserDTO {
	private final String email;
	private final String firstName;
	private final String lastName;
	private final String password;
	private final String phoneNumber;
	private final String sex;
	private final String photo;

	public UserDTO(String email, String firstName, String lastName, String password, String phoneNumber, String sex,
				   String photo) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.photo = photo;
	}
}

