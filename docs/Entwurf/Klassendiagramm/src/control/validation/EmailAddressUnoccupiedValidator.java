package control.validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.ibm.gsk.ikeyman.command.ValidatorFactory.Validator;
import com.ibm.security.validator.ValidatorException;

import business.service.ProfileService;

public class EmailAddressUnoccupiedValidator implements Validator<String> {
	
	private ProfileService profileService;
	
	@Override
    public void validate(FacesContext context, UIComponent component, String addresses) throws ValidatorException { }
	
}
