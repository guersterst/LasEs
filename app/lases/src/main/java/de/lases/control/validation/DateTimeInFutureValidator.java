package de.lases.control.validation;

import de.lases.business.service.UserService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * Validator for date inputs that validates if a given temporal (date, dateTime)
 * is in the future compared to the system time of the server. "Now" is
 * considered to be in the past here.
 */
@FacesValidator
public class DateTimeInFutureValidator implements Validator<Temporal> {

    /**
     * Validates a temporal as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param temporal the temporal to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Temporal temporal) throws ValidatorException {

    }
}
