package model.backing_beans;

import idk.SessionInformation;

public class RegistrationBacking {
	
	private String passwordInput;
	
	private String firstNameInput;
	
	private String lastNameInput;
	
	public String getPasswordInput() {
		return passwordInput;
	}

	public void setPasswordInput(String passwordInput) {
		this.passwordInput = passwordInput;
	}

	public String getFirstNameInput() {
		return firstNameInput;
	}

	public void setFirstNameInput(String firstNameInput) {
		this.firstNameInput = firstNameInput;
	}

	public String getLastNameInput() {
		return lastNameInput;
	}

	public void setLastNameInput(String lastNameInput) {
		this.lastNameInput = lastNameInput;
	}

	public String getTitleInput() {
		return titleInput;
	}

	public void setTitleInput(String titleInput) {
		this.titleInput = titleInput;
	}

	public String getEmailInput() {
		return emailInput;
	}

	public void setEmailInput(String emailInput) {
		this.emailInput = emailInput;
	}

	private String titleInput;
	
	private String emailInput;

	private SessionInformation sessionInformation;
	
	public String login() { return null; }

}
