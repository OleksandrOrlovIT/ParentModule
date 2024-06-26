package orlov.oleksandr.programming.citycountryspringrest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

/**
 * Country entity. Used in database.
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Country`s name cannot be empty")
    @Column(unique = true)
    private String countryName;

    @NotNull(message = "Country`s area cannot be null")
    @DecimalMin(value = "0.0", message = "Country`s area cannot be less then 0.0")
    private Double countryArea;

    @NotEmpty(message = "Country`s currency cannot be empty")
    private String currency;

    @JsonIgnore
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<City> cities;

    @Builder
    public Country(Long id, String countryName, Double countryArea, String currency, List<City> cities) {
        this.id = id;
        this.countryName = countryName;
        this.countryArea = countryArea;
        this.currency = currency;
        this.cities = cities;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Country country = (Country) o;
        return getId() != null && Objects.equals(getId(), country.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "countryName = " + countryName + ", " +
                "countryArea = " + countryArea + ", " +
                "currency = " + currency + ")";
    }
}