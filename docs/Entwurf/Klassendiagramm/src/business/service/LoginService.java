package business.service;

import javax.faces.bean.ApplicationScoped;

import business.util.Hashing;
import dtos.User;
import persistence.repository.UserRepository;

@ApplicationScoped
public class LoginService {

	private Event<UIMessage> uiMessageEvent;

	private Transaction transaction;

	public User login(String email, String password) { return null; }
}
