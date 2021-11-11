package business.service;

import business.util.AvatarUtil;
import dtos.Style;
import persistence.repository.SystemSettingsRepository;

@ApplicationScoped
public class CustomizationService {
	
	private AvatarUtil avatarUtil;

	private ExceptionQueue exceptionQueue;

	private Transaction transaction;

	public void setSystemSettings(Style style) { }

	public SystemSettings get() { }
	
	public void createDbSchema () { }
}
