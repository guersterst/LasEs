package de.lases.control.validation;

import de.lases.business.service.UserService;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.util.PropertyResourceBundle;

/**
 * Validator for date inputs that validates if a given local date
 * is in the future compared to the system time of the server. "Now" is
 * considered to be in the past here.
 */
@FacesValidator("dateTimeInFuture")
public class DateTimeInFutureValidator implements Validator<LocalDateTime> {

    private final PropertyResourceBundle messageResourceBundle = CDI.current().select(PropertyResourceBundle.class).get();

    /**
     * Validates a temporal as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param localDateTime the localDate to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, LocalDateTime localDateTime) throws ValidatorException {
        LocalDateTime time = LocalDateTime.now();

        if (!localDateTime.isAfter(time)) {
            String message = messageResourceBundle.getString("validateDeadlineReview");
            FacesMessage facesMessage = new FacesMessage(message);
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        }
    }
}
