package tqs.domus.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tqs.domus.restapi.model.House;

import java.util.List;


/**
 * @author João Vasconcelos
 * @date 16/mai/2020
 * @time 15:44
 */

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    @Query("SELECT h FROM House h WHERE (:city is null or h.city = :city) " +
            "and (:nRooms is null" + " or h.nRooms = :nRooms) " +
            "and (:minPrice is null" + " or h.price >= :minPrice) " +
            "and (:maxPrice is null" + " or h.price <= :maxPrice) " +
            "ORDER BY h.price ASC")
    List<House> findByAttributesAsc(@Param("city") String city, @Param("nRooms") Integer nRooms, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT h FROM House h WHERE (:city is null or h.city = :city) " +
            "and (:nRooms is null" + " or h.nRooms = :nRooms) " +
            "and (:minPrice is null" + " or h.price >= :minPrice) " +
            "and (:maxPrice is null" + " or h.price <= :maxPrice) " +
            "ORDER BY h.price ASC")
    List<House> findByAttributesDesc(@Param("city") String city, @Param("nRooms") Integer nRooms, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

}

