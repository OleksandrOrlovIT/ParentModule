package orlov.oleksandr.programming.citycountryspringrest.model.validators.annotaions;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Past;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Custom annotation for validating that a Year is in the past and not before a specified minimum year.
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@Past(message = "The founded year must be in the past")
public @interface PastWithMin {
    String message() default "{javax.validation.constraints.Past.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    int minBceYear() default -10000;
}