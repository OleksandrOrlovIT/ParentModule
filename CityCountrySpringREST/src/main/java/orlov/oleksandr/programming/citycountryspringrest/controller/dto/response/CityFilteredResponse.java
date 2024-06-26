package orlov.oleksandr.programming.citycountryspringrest.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

/**
 * DTO class for representing City data in a filtered response.
 */
@Setter
@Getter
@JsonFilter("cityFilter")
public class CityFilteredResponse {

    private Long id;

    private String cityName;

    private Long countryId;

    private Integer cityPopulation;

    private Double cityArea;

    private Year foundedAt;

    private String languages;
}
