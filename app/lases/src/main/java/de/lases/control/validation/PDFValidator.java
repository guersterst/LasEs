package de.lases.control.validation;

import de.lases.business.internal.ConfigPropagator;
import de.lases.control.internal.MessageResourceBundleProducer;
import de.lases.global.transport.FileDTO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.PropertyResourceBundle;
import java.util.Scanner;

/**
 * Validator for pdf files. Validates that a pdf does not overstep the maximum
 * file size for an uploaded pdf as specified in the configuration.
 */
@FacesValidator("pdfValidator")
public class PDFValidator implements Validator<FileDTO> {

    @Inject
    private ConfigPropagator configPropagator;

    @Inject
    private PropertyResourceBundle messageResourceBundle;

    private int convertLength = 1024;
    private int fileLength;
    //private int fileMax = Integer.parseInt(configPropagator.getProperty("MAX_PDF_FILE_SIZE_MB"));
    private int fileMax = 20;

    /**
     * Validates a pdf as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent  UIComponent we are checking for correctness
     * @param fileDTO      the file to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent,
                         FileDTO fileDTO) throws ValidatorException {
        fileLength = fileDTO.getFile().length;

        double fileSizeMB = (((double) fileLength / convertLength) / convertLength);

        String message = "";

        if (!is_pdf(fileDTO.getFile())) {
            message = messageResourceBundle.getString("validatePDFType");
        } else if (fileSizeMB >= fileMax){
            message = messageResourceBundle.getString("validatePDFSize");
        }

        if (!message.equals("")) {
            FacesMessage facesMessage = new FacesMessage(message);
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        }
    }

    private static boolean is_pdf(byte[] data) {
        if (data != null && data.length > 4
                && data[0] == 0x25 && // %
                data[1] == 0x50 && // P
                data[2] == 0x44 && // D
                data[3] == 0x46 && // F
                data[4] == 0x2D) { // -

            // version 1.3 file terminator
            if (//data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x33 &&
                    data[data.length - 7] == 0x25 && // %
                            data[data.length - 6] == 0x25 && // %
                            data[data.length - 5] == 0x45 && // E
                            data[data.length - 4] == 0x4F && // O
                            data[data.length - 3] == 0x46 && // F
                            data[data.length - 2] == 0x20 // SPACE
                //&& data[data.length - 1] == 0x0A// EOL
            ) {
                return true;
            }

            // version 1.3 file terminator
            if (//data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x34 &&
                    data[data.length - 6] == 0x25 && // %
                            data[data.length - 5] == 0x25 && // %
                            data[data.length - 4] == 0x45 && // E
                            data[data.length - 3] == 0x4F && // O
                            data[data.length - 2] == 0x46 // F
                //&& data[data.length - 1] == 0x0A // EOL
            ) {
                return true;
            }
        }
        return false;
    }
}
