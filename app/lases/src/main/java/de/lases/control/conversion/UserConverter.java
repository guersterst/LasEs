package de.lases.control.conversion;

import de.lases.business.service.UserService;
import de.lases.global.transport.User;
import de.lases.persistence.repository.UserRepository;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(forClass = User.class, managed = true)
public class UserConverter implements Converter<User> {

    @Inject
    private UserService userService;

    @Override
    public User getAsObject(FacesContext context, UIComponent component, String value) {
        User emailUser = new User();
        emailUser.setEmailAddress(value);
        return userService.get(emailUser);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, User value) {
        return value.getEmailAddress();
    }
}
