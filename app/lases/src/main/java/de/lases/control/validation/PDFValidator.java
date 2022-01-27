package de.lases.control.validation;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.FileDTO;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.PropertyResourceBundle;

/**
 * Validator for pdf files. Validates that a pdf does not overstep the maximum
 * file size for an uploaded pdf as specified in the configuration.
 *
 * @author Sebastian Vogt
 * @author Stefaie Gürster
 */
@FacesValidator("pdfValidator")
public class PDFValidator implements Validator<Part> {

    private final ConfigPropagator configPropagator = CDI.current().select(ConfigPropagator.class).get();

    private final PropertyResourceBundle messageResourceBundle
            = CDI.current().select(PropertyResourceBundle.class).get();

    private static final int CONVERT_LENGTH = 1024;
    private final int fileMax = Integer.parseInt(configPropagator.getProperty("MAX_PDF_FILE_SIZE_MB"));

    /**
     * Validates a pdf as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent  UIComponent we are checking for correctness
     * @param part         The file to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent,
                         Part part) throws ValidatorException {
        FileDTO fileDTO = new FileDTO();
        try {
            fileDTO.setFile(part.getInputStream().readAllBytes());
        } catch (IOException ex) {
            FacesMessage facesMessage = new FacesMessage(messageResourceBundle.getString("validatePDFIO"));
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        }

        int fileLength = fileDTO.getFile().length;

        double fileSizeMB = (((double) fileLength / CONVERT_LENGTH) / CONVERT_LENGTH);

        if (fileSizeMB >= fileMax){
            String message = messageResourceBundle.getString("validateFileSize");
            FacesMessage facesMessage = new FacesMessage(message + " " + fileMax + "MB");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        } else if (!part.getSubmittedFileName().endsWith(".pdf")) {
            FacesMessage facesMessage = new FacesMessage(messageResourceBundle.getString("validatePDFType"));
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        }

    }
}
