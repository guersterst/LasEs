package control.validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.ibm.gsk.ikeyman.command.ValidatorFactory.Validator;
import com.ibm.security.validator.ValidatorException;

@FacesValidator
public class PasswordValidator implements Validator<String> {
	
	private ProfileService profileService;
	
	@Override
    public void validate(FacesContext context, UIComponent component, String addresses) throws ValidatorException { }
	
}