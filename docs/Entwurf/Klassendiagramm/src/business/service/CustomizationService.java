package business.service;

import business.util.AvatarUtil;
import dtos.Style;
import persistence.repository.SystemSettingsRepository;

@ApplicationScoped
public class CustomizationService {
	
	private AvatarUtil avatarUtil;

	private SystemSettingsRepository systemSettingsRepository;

	public void setSystemSettings(Style style) { }

	public SystemSettings get() { }

	// TODO welches Repo hierfür?
	public void createDbSchema () { }

	public void setLogo() { }

	public void getLogo() { }
}
