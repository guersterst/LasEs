package de.lases.control.validation;

import de.lases.business.service.UserService;
import de.lases.global.transport.User;
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
 * Validator for email addresses that checks if a given email address is part
 * of the datasource.
 */
@FacesValidator
public class EmailAddressExistsValidator implements Validator<String> {

    private final Logger logger = Logger.getLogger(EmailAddressUnoccupiedValidator.class.getName());

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
        User user = new User();
        user.setEmailAddress(address);
        UserService userService = CDI.current().select(UserService.class).get();
        if (!userService.emailExists(user)) {
            PropertyResourceBundle bundle = CDI.current().select(PropertyResourceBundle.class).get();
            logger.finer("Validation failed: " + address + " does not exist.");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("emailNotExists"),
                    null);
            throw new ValidatorException(message);
        }
    }

}
