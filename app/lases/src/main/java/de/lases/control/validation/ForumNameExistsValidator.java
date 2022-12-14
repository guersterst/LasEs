package de.lases.control.validation;

import de.lases.business.service.ScientificForumService;
import de.lases.control.backing.ScientificForumBacking;
import de.lases.global.transport.ScientificForum;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.io.Serial;
import java.io.Serializable;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Validates whether a forum name exists and does not belong to the current forum.
 *
 * @author Johannes Garstenauer
 */
@FacesValidator
public class ForumNameExistsValidator implements Validator<String>, Serializable {

    @Serial
    private static final long serialVersionUID = -729646507861287388L;

    private final Logger logger = Logger.getLogger(EmailAddressUnoccupiedValidator.class.getName());

    /**
     * Validates whether a forum name exists and does not belong to the current forum.
     *
     * @throws ValidatorException if the name exists and does not belong to the current forum.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        ScientificForum nameForum = new ScientificForum();
        nameForum.setName(value);

        ScientificForumBacking scientificForumBacking = CDI.current().select(ScientificForumBacking.class).get();
        ScientificForum currentForum = scientificForumBacking.getForum();
        if (ScientificForumService.exists(nameForum) && !currentForum.getName().equals(nameForum.getName())) {
            PropertyResourceBundle bundle = CDI.current().select(PropertyResourceBundle.class).get();
            logger.finer("Validation failed: " + value + " forum name does already exist.");
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("forumNameTaken"), null);
            throw new ValidatorException(message);
        }
    }
}
