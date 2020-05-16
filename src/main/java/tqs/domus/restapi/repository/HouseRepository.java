package tqs.domus.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.domus.restapi.model.House;


/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 15:44
 */

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

}

