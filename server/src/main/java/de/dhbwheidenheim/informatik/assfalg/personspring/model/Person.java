package de.dhbwheidenheim.informatik.assfalg.personspring.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue 
	long id;
	String Benutzername, Passwort ;

	public String getName() {
		return Benutzername;
	}

	public void setName(String Benutzername) {
		this.Benutzername = Benutzername;
	}

	public String getPassword() {
		return Passwort;
	}

	public void setPassword(String Passwort) {
		this.Passwort = Passwort;
	}


	public Person(String Benutzername, String Passwort) {
		
		this.Benutzername = Benutzername;
		this.Passwort = Passwort;
	}
	
	public Person() {}

}
