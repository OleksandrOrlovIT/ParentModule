package orlov.oleksandr.programming.citycountryspringrest.csv;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.mapper.CityMapper;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityResponse;
import orlov.oleksandr.programming.citycountryspringrest.model.City;

import java.util.List;

/**
 * A class to generate a csv file
 */
@Component
@AllArgsConstructor
public class CSVGeneratorUtil {

    private static final String CSV_HEADER = "id,cityName,countryId,cityPopulation,cityArea,foundedAt,languages\n";

    private final CityMapper cityMapper;


    /**
     * A method to create a csv file from List of cities
     * @param cityList
     * @return
     */
    public String generateCityCsv(List<City> cityList) {
        List<CityResponse> cityResponses = cityMapper.toCityResponseListFormatCSV(cityList);

        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for (CityResponse city : cityResponses) {
            csvContent.append(city.getId()).append(",")
                    .append(city.getCityName()).append(",")
                    .append(city.getCountryId()).append(",")
                    .append(city.getCityPopulation()).append(",")
                    .append(city.getCityArea()).append(",")
                    .append(city.getFoundedAt()).append(",")
                    .append(city.getLanguages()).append("\n");
        }

        return csvContent.toString();
    }
}
