package business.service;

import business.util.AvatarUtil;
import dtos.Style;
import persistence.repository.SystemSettingsRepository;

public class CustomizationService {
	
	private AvatarUtil avatarUtil;

	private ExceptionQueue exceptionQueue;

	private SystemSettingsRepository systemSettingsRepository;
	
	public void setSystemSettings(Style style) { }

	public SystemSettings get() { }
	
	public void createDbSchema () { }
}
