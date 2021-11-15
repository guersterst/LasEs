package business.service;

import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transaction;
import javax.enterprise.event.Event;
import business.util.EmailUtil;
import business.util.Hashing;
import dtos.User;
import persistence.repository.UserRepository;

@ApplicationScoped
public class RegistrationService {

	private Event<UIMessage> uiMessageEvent;

	private Transaction transaction;
	
	public User selfRegister(User user) { return null; }
	
	public void registerByAdmin(User user) { }

}
