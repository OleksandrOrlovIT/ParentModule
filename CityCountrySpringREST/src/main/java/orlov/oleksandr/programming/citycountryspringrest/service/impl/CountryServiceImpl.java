package orlov.oleksandr.programming.citycountryspringrest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;
import orlov.oleksandr.programming.citycountryspringrest.repository.CountryRepository;
import orlov.oleksandr.programming.citycountryspringrest.service.interfaces.CountryService;
import orlov.oleksandr.programming.citycountryspringrest.service.messages.MessageSender;

import java.util.List;
import java.util.Objects;

/**
 * Implementation for CountryService
 */
@AllArgsConstructor
@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final MessageSender messageSender;

    /**
     * Retrieves all countries.
     *
     * @return List of countries
     */
    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    /**
     * Retrieves a country by its ID.
     *
     * @param id The ID of the country to retrieve
     * @return The country with the given ID
     * @throws EntityNotFoundException if the country with the given ID is not found
     */
    @Override
    public Country findById(Long id) {
        Objects.requireNonNull(id, "Country's id must not be null");

        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country with id " + id + " not found"));
    }

    /**
     * Creates a new country.
     *
     * @param country The country to create
     * @return The created country
     * @throws IllegalArgumentException if the country already exists or if the country's ID is not null
     */
    @Override
    public Country create(Country country) {
        checkCountryAndNameForNull(country);

        if (country.getId() != null) {
            throw new IllegalArgumentException("Country id must be null");
        }

        if (existsByName(country.getCountryName())) {
            throw new IllegalArgumentException("Country with name = " + country.getCountryName() + " already exists");
        }

        Country savedCountry = countryRepository.save(country);

        if(savedCountry.getId() != null){
            messageSender.sendMessageWithEmail("Successfully created country.", savedCountry.toString());
        }

        return savedCountry;
    }

    /**
     * Updates an existing country.
     *
     * @param country The country to update
     * @return The updated country
     * @throws IllegalArgumentException if the country's name is null or if the country with the given name already exists
     */
    @Override
    public Country update(Country country) {
        checkCountryAndNameForNull(country);

        Country foundCountry = findById(country.getId());

        if(!foundCountry.getCountryName().equals(country.getCountryName()) && existsByName(country.getCountryName())){
            throw new IllegalArgumentException("Country with name= " + country.getCountryName() + " already exists");
        }

        return countryRepository.save(country);
    }

    /**
     * Deletes a country by its ID.
     *
     * @param id The ID of the country to delete
     */
    @Override
    public void deleteById(Long id) {
        countryRepository.deleteById(id);
    }

    /**
     * Checks if a country already exists by its name.
     *
     * @param countryName The name of the country to check
     * @return true if the country already exists, false otherwise
     */
    @Override
    public boolean existsByName(String countryName) {
        return countryRepository.existsByCountryName(countryName);
    }

    /**
     * Checks if the country or its name is null.
     *
     * @param country The country to check
     * @throws NullPointerException if the country or its name is null
     */
    private void checkCountryAndNameForNull(Country country) {
        Objects.requireNonNull(country, "Country must not be null");
        Objects.requireNonNull(country.getCountryName(), "Country name must not be null");
    }
}
