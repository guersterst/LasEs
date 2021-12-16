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

import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

@FacesValidator
public class ForumNameExistsValidator implements Validator<String> {

    private final Logger l = Logger.getLogger(EmailAddressUnoccupiedValidator.class.getName());

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        ScientificForum forum = new ScientificForum();
        forum.setName(value);

        if (ScientificForumService.exists(forum) || Objects.equals(value, "") || Objects.equals(value, null)) {
            PropertyResourceBundle bundle = CDI.current().select(PropertyResourceBundle.class).get();
            l.finer("Validation failed: " + value + " forum name does already exist.");
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("forumNameTaken"), null);
            throw new ValidatorException(message);
        }
    }
    //TODO javadoc
}
