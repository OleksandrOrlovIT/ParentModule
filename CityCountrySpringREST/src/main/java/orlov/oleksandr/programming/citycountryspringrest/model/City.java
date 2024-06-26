package orlov.oleksandr.programming.citycountryspringrest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orlov.oleksandr.programming.citycountryspringrest.model.validators.annotaions.PastWithMin;

import java.time.Year;
import java.util.List;
import java.util.Objects;

/**
 * City entity. Used in database.
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "City name cannot be empty")
    private String cityName;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @NotNull(message = "City`s country cannot be null")
    private Country country;

    @NotNull(message = "City`s population cannot be null")
    @Min(value = 0, message = "City`s population cannot be less then 0")
    private Integer cityPopulation;

    @NotNull(message = "City`s area cannot be null")
    @DecimalMin(value = "0.0", message = "City`s area cannot be less then 0.0")
    private Double cityArea;

    @PastWithMin(message = "The founded year must be in the past and can't be less then 10000 BCE")
    private Year foundedAt;

    @ElementCollection
    @NotEmpty(message = "City has to have at least one language of speaking")
    private List<String> languages;

    @Builder
    public City(Long id, String cityName, Country country, Integer cityPopulation, Double cityArea, Year foundedAt,
                List<String> languages) {
        this.id = id;
        this.cityName = cityName;
        this.country = country;
        this.cityPopulation = cityPopulation;
        this.cityArea = cityArea;
        this.foundedAt = foundedAt;
        this.languages = languages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;
        return Objects.equals(id, city.id)
                && Objects.equals(cityName, city.cityName)
                && Objects.equals(country, city.country)
                && Objects.equals(cityPopulation, city.cityPopulation)
                && Objects.equals(cityArea, city.cityArea) && Objects.equals(foundedAt, city.foundedAt)
                && Objects.equals(languages, city.languages);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(cityName);
        result = 31 * result + Objects.hashCode(country);
        result = 31 * result + Objects.hashCode(cityPopulation);
        result = 31 * result + Objects.hashCode(cityArea);
        result = 31 * result + Objects.hashCode(foundedAt);
        result = 31 * result + Objects.hashCode(languages);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "cityName = " + cityName + ", " +
                "country = " + country + ", " +
                "cityPopulation = " + cityPopulation + ", " +
                "cityArea = " + cityArea + ", " +
                "foundedAt = " + foundedAt + ", " +
                "languages = " + languages + ")";
    }
}