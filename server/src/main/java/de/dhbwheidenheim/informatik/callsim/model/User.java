package de.dhbwheidenheim.informatik.callsim.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {

	@Id
	@GeneratedValue 
	long id;
	String Username, Passwort ;

	public String getUsername() {
		return Username;
	}

	public void setName(String Username) {
		this.Username = Username;
	}

	public String getPassword() {
		return Passwort;
	}

	public void setPassword(String Passwort) {
		this.Passwort = Passwort;
	}


	public User(String Username, String Passwort) {
		
		this.Username = Username;
		this.Passwort = Passwort;
	}
	
	public User() {}

}
