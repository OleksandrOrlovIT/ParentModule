package orlov.oleksandr.programming.citycountryspringrest.service.interfaces;

import orlov.oleksandr.programming.citycountryspringrest.model.Country;

/**
 * Interface for Country Service
 */
public interface CountryService extends CrudService<Country, Long>{
    boolean existsByName(String countryName);
}
