package orlov.oleksandr.programming.citycountryspringrest.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Year;

/**
 * DTO class for representing City data in a standard response.
 */
@Setter
@Getter
public class CityResponse {
    private Long id;

    private String cityName;

    private Long countryId;

    private Integer cityPopulation;

    private Double cityArea;

    private Year foundedAt;

    private String languages;
}
