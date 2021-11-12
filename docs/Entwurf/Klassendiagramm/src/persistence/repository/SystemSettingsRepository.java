package persistence.repository;

import dtos.File;
import dtos.Style;
import dtos.SystemSettings;

public class SystemSettingsRepository {
	
	public static void updateSettings(SystemSettings systemSettings, Transaction transaction) { }
	
	public static SystemSettings getSettings(Transaction transaction) { return null; }
	
	public static File getLogo() { return null; }
	public static void setLogo() { }

}
