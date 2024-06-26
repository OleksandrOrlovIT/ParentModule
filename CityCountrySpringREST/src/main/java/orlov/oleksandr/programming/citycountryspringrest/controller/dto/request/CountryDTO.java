package orlov.oleksandr.programming.citycountryspringrest.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class for representing Country data.
 */
@Setter
@Getter
public class CountryDTO {
    @NotEmpty(message = "Country`s name cannot be empty")
    private String countryName;

    @NotNull(message = "Country`s area cannot be null")
    private Double countryArea;

    @NotEmpty(message = "Country`s currency cannot be empty")
    private String currency;

    @Builder
    public CountryDTO(String countryName, Double countryArea, String currency) {
        this.countryName = countryName;
        this.countryArea = countryArea;
        this.currency = currency;
    }
}
