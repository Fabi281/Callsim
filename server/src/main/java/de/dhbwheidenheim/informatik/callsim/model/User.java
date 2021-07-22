package de.dhbwheidenheim.informatik.callsim.model;

import java.io.Serializable;

public class User implements Serializable {

	String Username, Password;

	public String getUsername() {
		return Username;
	}

	public void setName(String Username) {
		this.Username = Username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String Password) {
		this.Password = Password;
	}


	public User(String Username, String Password) {
		
		this.Username = Username;
		this.Password = Password;
	}
	
	public User() {}
}
