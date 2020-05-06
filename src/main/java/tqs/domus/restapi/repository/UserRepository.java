package tqs.domus.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.domus.restapi.model.User;

/**
 * @author Vasco Ramos
 * @date 06/mai/2020
 * @time 13:36
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * @param email
	 * @return
	 */
	User findByEmail(String email);

	Boolean existsByEmail(String email);

}

