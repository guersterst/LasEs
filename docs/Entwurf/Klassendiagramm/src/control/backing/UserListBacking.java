package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.ProfileService;
import control.internal.SessionInformation;
import dtos.Privilege;
import dtos.User;

@RequestScoped
public class UserListBacking {

	private SessionInformation sessionInformation;

	private ProfileService profileService;

	private Privilege privilege;

	private String searchText;
	
	
	@PostConstruct
	public void init() { }

	public String applyFilter(){ return null; }

	public void nameUp(){ }

	public void nameDown(){ }

	public void roleUp(){ }

	public void roleDown(){ }

	public List<User> getUserList(){ return null;}



}
