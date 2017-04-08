package budget.accessories.validators;

import budget.accessories.validators.interfaces.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Created by veghe on 23/11/2016.
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(value).find();
    }
}
