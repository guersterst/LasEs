package business.service;

import business.util.AvatarUtil;
import dtos.Style;
import persistence.repository.SystemSettingsRepository;

@ApplicationScoped
public class CustomizationService {
	
	private AvatarUtil avatarUtil;

	public void setSystemSettings(Style style) { }

	public SystemSettings get() { }

	public void createDbSchema () { }

	public void getConnectionState() { }

	public void setLogo() { }

	public void getLogo() { }
}
