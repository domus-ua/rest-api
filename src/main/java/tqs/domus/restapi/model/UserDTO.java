package tqs.domus.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Vasco Ramos
 * @date 06/05/20
 * @time 11
 */

@Getter
@AllArgsConstructor
public class UserDTO {
	private final String email;
	private final String firstName;
	private final String lastName;
	private final String password;
	private final String phoneNumber;
	private final String sex;
	private final String photo;
}

