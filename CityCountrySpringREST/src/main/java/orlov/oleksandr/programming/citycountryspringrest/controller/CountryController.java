package orlov.oleksandr.programming.citycountryspringrest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.mapper.CountryMapper;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.request.CountryDTO;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;
import orlov.oleksandr.programming.citycountryspringrest.service.interfaces.CountryService;

import java.util.List;

/**
 * A controller to manage Country entities
 */
@CrossOrigin("http://localhost:3000/")
@AllArgsConstructor
@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;
    private final CountryMapper countryMapper;

    /**
     * Endpoint to get all countries
     * @return
     */
    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.findAll();
    }

    /**
     * An endpoint to create a country
     * @param countryDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Country> createCountry(@RequestBody @Validated CountryDTO countryDTO) {
        Country country = countryMapper.toCountry(countryDTO);

        return new ResponseEntity<>(countryService.create(country), HttpStatus.CREATED);
    }


    /**
     * An endpoint to update a country
     * @param countryId
     * @param countryDTO
     * @return
     */
    @PutMapping("/{countryId}")
    public Country updateCountry(@PathVariable Long countryId, @RequestBody @Validated CountryDTO countryDTO) {
        Country country = countryMapper.toCountry(countryDTO);
        country.setId(countryId);
        return countryService.update(country);
    }

    /**
     * An endpoint to delete a country
     * @param countryId
     */
    @DeleteMapping("/{countryId}")
    public void deleteCountry(@PathVariable Long countryId) {
        countryService.deleteById(countryId);
    }
}

