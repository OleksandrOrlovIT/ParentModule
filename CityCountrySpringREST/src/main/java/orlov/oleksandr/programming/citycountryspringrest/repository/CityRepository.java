package orlov.oleksandr.programming.citycountryspringrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import orlov.oleksandr.programming.citycountryspringrest.model.City;

import java.util.List;

/**
 * JpaRepository and JpaSpecificationExecutor Interface to manage Cities
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> , JpaSpecificationExecutor<City> {
    List<City> findByCityName(String name);
}