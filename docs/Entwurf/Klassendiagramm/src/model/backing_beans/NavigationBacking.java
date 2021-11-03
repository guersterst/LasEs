package model.backing_beans;

import idk.SessionInformation;

public class NavigationBacking {
	
	private String searchString;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	private SessionInformation sessionInformation;
	
	public String logout() { return null; }
	
	public String search() { return null; }

}
