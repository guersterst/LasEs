package de.lases.control.validation;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;

import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Validator for passwords that validates password after the following rules:
 * <ul>
 *     <li>
 *         must contain both uppercase and lowercase letters
 *     </li>
 *     <li>
 *         must contain numbers
 *     </li>
 *     <li>
 *         must contain special characters
 *     </li>
 *     <li>
 *         must be between 8 and 100 characters in length
 *     </li>
 * </ul>
 */
@FacesValidator
public class PasswordValidator implements Validator<String> {

    private final Logger l = Logger.getLogger(PasswordValidator.class.getName());

    PropertyResourceBundle bundle = CDI.current().select(PropertyResourceBundle.class).get();

    /**
     * Validates passwords as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param password the address to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent,
                         String password) throws ValidatorException {
        // Check if password is null
        boolean valid = password != null;

        // Check if password length is between 8 and 100 characters
        valid = valid && password.length() >= 8 && password.length() <= 100;

        // Check if password contains uppercase letters
        valid = valid && password.chars().anyMatch(Character::isUpperCase);

        // Check if password contains lowercase letters
        valid = valid && password.chars().anyMatch(Character::isLowerCase);

        // Check if password contains numbers
        valid = valid && password.chars().anyMatch(Character::isDigit);

        // Check if password contains special characters
        valid = valid && password.chars().anyMatch(c -> !Character.isAlphabetic(c) && !Character.isDigit(c));

        // Throw exception if password is not valid
        if (!valid) {
            l.finer("Validation failed: " + password + " is an invalid password");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    bundle.getString("invalidPassword"), null);
            throw new ValidatorException(message);
        }
    }

}