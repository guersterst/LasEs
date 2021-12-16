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
import jakarta.inject.Inject;
import jakarta.servlet.http.Part;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.PropertyResourceBundle;

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
 *
 * @author Sebastian Vogt
 */
@FacesValidator("avatarValidator")
public class AvatarValidator implements Validator<Part> {

    private final PropertyResourceBundle messageResourceBundle
            = CDI.current().select(PropertyResourceBundle.class).get();
    private final ConfigPropagator configPropagator = CDI.current().select(ConfigPropagator.class).get();
    private final int fileMax = Integer.parseInt(configPropagator.getProperty("MAX_AVATAR_FILE_SIZE_MB"));

    private static final int CONVERT_LENGTH = 1024;

    /**
     * Validates an avatar as specified in the class description.
     *
     * @param facesContext FacesContext for the request we are processing
     * @param uiComponent UIComponent we are checking for correctness
     * @param avatar The file to validate
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent,
                         Part avatar) throws ValidatorException {

        long fileLength = avatar.getSize();

        double fileSizeMB = (((double) fileLength / CONVERT_LENGTH) / CONVERT_LENGTH);

        if (fileSizeMB >= fileMax) {
            String message = messageResourceBundle.getString("validateFileSize");
            FacesMessage facesMessage = new FacesMessage(message + " " + fileMax + "MB");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        }

        try {
            BufferedImage image = ImageIO.read(avatar.getInputStream());
            if (image == null) {
                String message = messageResourceBundle.getString("wrongImageType");
                FacesMessage facesMessage = new FacesMessage(message);
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(facesMessage);
            } else if ((double) image.getHeight() / image.getWidth() > 2
                    || (double) image.getHeight() / image.getWidth() < 0.5) {
                String message = messageResourceBundle.getString("wrongImageRatio");
                FacesMessage facesMessage = new FacesMessage(message);
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(facesMessage);
            }
        } catch (IOException e) {
            String message = messageResourceBundle.getString("dataNotWritten");
            FacesMessage facesMessage = new FacesMessage(message);
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        }

    }
}
