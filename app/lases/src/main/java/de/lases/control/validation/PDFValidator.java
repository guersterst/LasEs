package de.lases.control.validation;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.servlet.http.Part;

@FacesValidator
public class PDFValidator implements Validator<Part> {

    @Override
    public void validate(FacesContext context, UIComponent component, Part pdf) throws ValidatorException {
    }

}
