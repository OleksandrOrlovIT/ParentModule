package orlov.oleksandr.programming.citycountryspringrest.controller.dto.mapper;

import org.springframework.stereotype.Component;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.request.CountryDTO;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;

@Component
public class CountryMapper {

    /**
     * Maps a CountryDTO to a Country entity.
     *
     * @param countryDTO The CountryDTO object
     * @return The mapped Country entity
     */
    public Country toCountry(CountryDTO countryDTO){
        Country country = new Country();
        country.setCountryName(countryDTO.getCountryName());
        country.setCountryArea(countryDTO.getCountryArea());
        country.setCurrency(countryDTO.getCurrency());
        return country;
    }
}
