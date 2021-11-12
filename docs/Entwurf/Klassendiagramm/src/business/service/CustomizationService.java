package business.service;

import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transaction;
import javax.enterprise.event.Event;
import business.util.AvatarUtil;
import dtos.Style;
import dtos.SystemSettings;
import persistence.repository.SystemSettingsRepository;

@ApplicationScoped
public class CustomizationService {

	private Event<UIMessage> uiMessageEvent;

	private Transaction transaction;

	public void setSystemSettings(Style style) { }

	public void get() { }

	public void createDbSchema () { }

	public void getConnectionState() { }

	public void setLogo() { }

	public void getLogo() { }
}
