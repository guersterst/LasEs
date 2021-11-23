package de.lases.control.validation;

import de.lases.global.transport.FileDTO;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

/**
 * Validator for the avatar file. Validates an avatar in the following way:
 * <ul>
 *     <li>
 *         the maximum file size for an uploaded image as specified in the
 *         configuration cannot not be overstepped.
 *     </li>
 *     <li>
 *         the image can be interpreted as an image by the
 *         {@code javax.imageio} package
 *     </li>
 *     <li>
 *         The ration of width to height cannot be above 2 : 1 or below 1 : 2
 *     </li>
 * </ul>
 */
@FacesValidator
public class AvatarValidator implements Validator<FileDTO> {

    /**
     * Validates an avatar as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param fileDTO the file to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent,
                         FileDTO fileDTO) throws ValidatorException {

    }
}
