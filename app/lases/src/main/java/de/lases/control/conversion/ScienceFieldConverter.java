package de.lases.control.conversion;

import de.lases.global.transport.ScienceField;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(forClass = ScienceField.class)
public class ScienceFieldConverter implements Converter<ScienceField> {
    //TODO fehlerfall und facesmsgs
    @Override
    public ScienceField getAsObject(FacesContext context, UIComponent component, String value) {
        ScienceField scienceField = new ScienceField();
        scienceField.setName(value);
        return scienceField;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ScienceField value) {
        return value.getName();
    }
}

