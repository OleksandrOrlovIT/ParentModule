package orlov.oleksandr.programming.citycountryspringrest.json;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.request.CityDTO;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.mapper.CityMapper;
import orlov.oleksandr.programming.citycountryspringrest.model.City;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;
import orlov.oleksandr.programming.citycountryspringrest.service.interfaces.CityService;
import orlov.oleksandr.programming.citycountryspringrest.service.interfaces.CountryService;

import java.io.IOException;
import java.io.InputStream;

/**
 * Parses JSON data containing city information and saves it to the database.
 */
@AllArgsConstructor
@Component
@Slf4j
public class JSONParser {

    private CityService cityService;
    private CountryService countryService;
    private CityMapper cityMapper;

    /**
     * Creates a data supplier for reading city data from an input stream.
     *
     * @param file JSON file containing city data
     * @return InputStreamJsonArrayStreamDataSupplier instance
     */
    private static InputStreamJsonArrayStreamDataSupplier<CityDTO> getDataSupplier(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            return new InputStreamJsonArrayStreamDataSupplier<>(CityDTO.class, inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading city names", e);
        }
    }

    /**
     * Reads city data from a JSON file and saves it to the database.
     *
     * @param file JSON file containing city data
     * @return An array with two elements: the total number of cities processed and the number of cities saved
     */
    public int[] saveCitiesFromInputStream(MultipartFile file) {
        var supplier = getDataSupplier(file);
        //An array with 2 elements -> First element all times, and second only saved times
        final int[] allTimesAndSaved = new int[]{0, 0};

        supplier.get()
                .forEach(cityDTO -> {
                    log.info("Saving city using cityDTO {}", cityDTO);
                    try {
                        Country foundCountry = countryService.findById(cityDTO.getCountryId());
                        City savedCity = cityService.create(cityMapper.toCity(cityDTO, foundCountry));
                        if (savedCity != null) {
                            allTimesAndSaved[1]++;
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    } finally {
                        allTimesAndSaved[0]++;
                    }
                });

        return allTimesAndSaved;
    }
}