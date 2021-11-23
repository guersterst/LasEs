package de.lases.control.validation;

import de.lases.business.service.ScientificForumService;
import de.lases.business.service.UserService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;

/**
 * Validator for email addresses that checks if a given forum name is part
 * of the datasource.
 */
@FacesValidator
public class ForumNameExistsValidator implements Validator<String> {

    @Inject
    private ScientificForumService scientificForumService;

    /**
     * Validates forum name as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param name the address to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, String name) throws ValidatorException {
    }

}
