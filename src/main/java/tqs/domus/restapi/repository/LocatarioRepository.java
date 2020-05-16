package tqs.domus.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.domus.restapi.model.Locatario;
import tqs.domus.restapi.model.User;

/**
 * @author Vasco Ramos
 * @date 16/mai/2020
 * @time 10:52
 */

@Repository
public interface LocatarioRepository extends JpaRepository<Locatario, Long> {
	Locatario findByUser(User user);

	boolean existsByUser(User user);
}
