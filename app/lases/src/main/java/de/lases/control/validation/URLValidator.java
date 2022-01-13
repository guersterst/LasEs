package de.lases.control.validation;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;

import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for urls that checks if the given url is a valid
 * url.
 */
@FacesValidator
public class
URLValidator implements Validator<String> {

    @Inject
    private PropertyResourceBundle resourceBundle;

    private static final String URL_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    /**
     * Validates an url as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param url the url to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent,
                         String url) throws ValidatorException {

        Pattern urlPattern = Pattern.compile(URL_PATTERN);
        Matcher matcher = urlPattern.matcher(url);

        if (!matcher.find()) {

            FacesMessage facesMessage = new FacesMessage(resourceBundle.getString("validateUrl"));
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);

        }

    }
}
