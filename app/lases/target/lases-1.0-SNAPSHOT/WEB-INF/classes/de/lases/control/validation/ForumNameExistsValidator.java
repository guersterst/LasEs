package de.lases.control.validation;

import de.lases.business.service.*;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator
public class ForumNameExistsValidator implements Validator<String> {

    private UserService userService;

    @Override
    public void validate(FacesContext context, UIComponent component, String addresses) throws ValidatorException {
    }

}
