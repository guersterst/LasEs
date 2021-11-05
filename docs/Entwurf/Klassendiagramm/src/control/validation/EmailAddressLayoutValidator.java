package control.validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import business.util.EmailUtil;

public class EmailAddressLayoutValidator implements Validator<String> {


    @Override
    public void validate(FacesContext context, UIComponent component, String addresses) throws ValidatorException { }


}
