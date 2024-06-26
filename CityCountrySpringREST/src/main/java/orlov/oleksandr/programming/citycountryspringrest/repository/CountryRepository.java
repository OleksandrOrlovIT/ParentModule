package orlov.oleksandr.programming.citycountryspringrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;

import java.util.Optional;

/**
 * JpaRepository Interface to manage Countries
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByCountryName(String name);
}
