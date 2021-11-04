package control.validation;

import business.service.ProfileService;

public class EmailAddressUnoccupiedValidator implements Validator <String> {
	
	private ProfileService profileService;
	
	@Override
    public void validate(FacesContext context, UIComponent component, String addresses) throws ValidatorException { }
	
}
