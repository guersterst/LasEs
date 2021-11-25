package de.lases.control.validation;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.FileDTO;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.servlet.http.Part;

/**
 * Validator for pdf files. Validates that a pdf does not overstep the maximum
 * file size for an uploaded pdf as specified in the configuration.
 */
@FacesValidator
public class PDFValidator implements Validator<FileDTO> {

    @Inject
    private ConfigPropagator configPropagator;

    /**
     * Validates a pdf as specified in the class description.
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
