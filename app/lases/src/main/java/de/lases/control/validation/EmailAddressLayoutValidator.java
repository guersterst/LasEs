package de.lases.control.validation;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Validator for email addresses that checks if the given email is a valid
 * email address.
 * @author Thomas Kirz
 */
@FacesValidator
public class EmailAddressLayoutValidator implements Validator<String> {

    /**
     * E-Mail address regular expression from
     * <a href=https://owasp.org/www-community/OWASP_Validation_Regex_Repository>OWASP Validation Regex Repository</a>
     */
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final Logger logger = Logger.getLogger(EmailAddressLayoutValidator.class.getName());

    private final PropertyResourceBundle bundle = CDI.current().select(PropertyResourceBundle.class).get();

    /**
     * Validates an email address as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param address the address to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent,
                         String address) throws ValidatorException {
        if (address == null || !address.matches(EMAIL_REGEX)) {
            logger.finer("Validation failed: Email address " + address + " is invalid");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("emailInvalid"),
                    null);
            throw new ValidatorException(message);
        }
    }


}
