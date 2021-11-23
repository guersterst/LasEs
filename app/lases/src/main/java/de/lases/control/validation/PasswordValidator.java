package de.lases.control.validation;

import de.lases.business.service.UserService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

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
    }

}