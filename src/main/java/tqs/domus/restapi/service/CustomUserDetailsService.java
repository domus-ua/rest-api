package tqs.domus.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tqs.domus.restapi.model.User;
import tqs.domus.restapi.model.UserAuth;
import tqs.domus.restapi.repository.UserRepository;

/**
 * @author Vasco Ramos
 * @date 05/05/20
 * @time 22
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = repository.findByEmail(username);
		UserAuth userDetails;
		if (user != null) {
			userDetails = new UserAuth();
			userDetails.setUser(user);
		} else {
			throw new UsernameNotFoundException("User not exist with name : " + username);
		}
		return userDetails;

	}
}

