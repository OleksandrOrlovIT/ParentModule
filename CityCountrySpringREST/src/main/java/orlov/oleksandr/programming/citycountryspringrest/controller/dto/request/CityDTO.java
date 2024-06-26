package orlov.oleksandr.programming.citycountryspringrest.controller.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orlov.oleksandr.programming.citycountryspringrest.json.deserializer.YearDeserializer;
import orlov.oleksandr.programming.citycountryspringrest.model.validators.annotaions.PastWithMin;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DTO class for representing City data.
 */
@Setter
@Getter
@NoArgsConstructor
public class CityDTO {
    @NotEmpty(message = "City name cannot be empty")
    private String cityName;

    @NotNull(message = "Country`s id cannot be null")
    private Long countryId;

    @NotNull(message = "City`s population cannot be null")
    @Min(value = 0, message = "City`s population cannot be less then 0")
    private Integer cityPopulation;

    @NotNull(message = "City`s area cannot be null")
    @DecimalMin(value = "0.0", message = "City`s area cannot be less then 0.0")
    private Double cityArea;

    @JsonDeserialize(using = YearDeserializer.class)
    @PastWithMin(message = "The founded year must be in the past and can't be less then 10000 BCE")
    private Year foundedAt;

    @NotEmpty(message = "City has to have at least one language of speaking")
    private String languages;

    public List<String> getLanguagesList() {
        if (languages == null || languages.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(languages.split(",\\s*")));
        }
    }

    @Override
    public String toString() {
        return "CityDTO{" +
                "cityName='" + cityName + '\'' +
                ", countryId=" + countryId +
                ", cityPopulation=" + cityPopulation +
                ", cityArea=" + cityArea +
                ", foundedAt=" + foundedAt +
                ", languages='" + languages + '\'' +
                '}';
    }
}