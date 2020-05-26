package tqs.domus.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.repository.UserRepository;

/**
 * @author Vasco Ramos
 * @date 26/mai/2020
 * @time 10:50
 */

@Service
public class AdminService {

	@Autowired
	private UserRepository userRepository;

	public boolean clearDatabase() {
		try {
			userRepository.deleteAll();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
