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

}

