package orlov.oleksandr.programming.citycountryspringrest.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Year;

/**
 * Converts Year objects to and from database columns of type Integer.
 */
@Converter
public class YearConverter implements AttributeConverter<Year, Integer> {

    /**
     * Converts a Year object to an Integer representing the year value.
     *
     * @param year The Year object to convert
     * @return Integer representing the year value
     */
    @Override
    public Integer convertToDatabaseColumn(Year year) {
        return year == null ? null : year.getValue();
    }

    /**
     * Converts an Integer representing the year value to a Year object.
     *
     * @param dbData The Integer value representing the year
     * @return Year object
     */
    @Override
    public Year convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Year.of(dbData);
    }
}