package orlov.oleksandr.programming.citycountryspringrest.controller.dto.response;

import lombok.Getter;
import lombok.Setter;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;

import java.time.Year;

/**
 * DTO class for representing City data in CRUD operations.
 */
@Getter
@Setter
public class CityCRUDResponse {
    private Long id;

    private String cityName;

    private Country country;

    private Integer cityPopulation;

    private Double cityArea;

    private Year foundedAt;

    private String languages;
}
