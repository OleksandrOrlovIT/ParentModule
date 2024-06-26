package orlov.oleksandr.programming.citycountryspringrest.model.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import orlov.oleksandr.programming.citycountryspringrest.model.validators.annotaions.PastWithMin;

import java.time.Year;


/**
 * Validator for PastWithMin annotation.
 */
public class PastWithMinValidator implements ConstraintValidator<PastWithMin, Year> {

    private int minBceYear;

    /**
     * Initializes the validator with the specified minimum BCE year.
     *
     * @param constraintAnnotation The annotation instance
     */
    @Override
    public void initialize(PastWithMin constraintAnnotation) {
        this.minBceYear = constraintAnnotation.minBceYear();
    }

    /**
     * Validates if the given Year is in the past and not before the minimum BCE year.
     *
     * @param value   The Year to validate
     * @param context The validation context
     * @return true if the Year is valid, false otherwise
     */
    @Override
    public boolean isValid(Year value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        int currentYear = Year.now().getValue();

        return value.getValue() < currentYear && value.getValue() >= minBceYear;
    }
}