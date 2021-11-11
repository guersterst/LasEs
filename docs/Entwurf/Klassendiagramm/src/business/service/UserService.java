package business.service;

import java.util.List;

import javax.enterprise.event.Event;
import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transaction;

import business.util.AvatarUtil;
import business.util.EmailUtil;
import control.validation.EmailAddressUnoccupiedValidator;
import de.uni_passau.fim.einfuehrung.einfuehrungsaufgabe_garstenauer_2.model.UIMessage;
import dtos.User;
import global.util.ResultListParameters;
import persistence.repository.UserRepository;

@ApplicationScoped
public class UserService {

	private Event<UIMessage> uiMessageEvent;

	private Transaction transaction;

	public User getUser(User user) { }

	public void editUser(User newUser) { }

	public void deleteUser(String email) { }

	public boolean emailExists(String email) { return false; }

	// byte[] gut?
	public void setAvatar() { }

	public File getAvatar() { }

	public void deleteAvatar(String email) { }

	public List<User> getUsers(User user, ResultListParameters resultListParams) { return null; }
}
