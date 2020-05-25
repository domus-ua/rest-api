package tqs.domus.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.domus.restapi.model.Contract;

/**
 * @author Vasco Ramos
 * @date 25/mai/2020
 * @time 15:46
 */

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
