package tqs.domus.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 14:12
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ErrorDetails extends Exception {

	public ErrorDetails(String message) {
		super(message);
	}
}
