package business.service;

import java.util.List;

import javax.faces.bean.ApplicationScoped;

import business.util.AvatarUtil;
import business.util.EmailUtil;
import control.validation.EmailAddressUnoccupiedValidator;
import dtos.User;
import global.util.ResultListParameters;
import persistence.repository.UserRepository;

@ApplicationScoped
public class UserService {

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
