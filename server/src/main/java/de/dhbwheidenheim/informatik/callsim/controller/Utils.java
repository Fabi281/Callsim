package de.dhbwheidenheim.informatik.callsim.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbwheidenheim.informatik.callsim.model.User;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static void writeToFile(User user, String location) {
		try {

			ArrayList<User> users = new ArrayList<User>();
			users.add(user);

			FileOutputStream fos = new FileOutputStream(location);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            fos.close();

			log.info("Data saved at file location: " + location);
		} catch (IOException e) {
			log.info("Hmm.. Got an error while saving data to file " + e.toString());
		}
	}
 
	public static ArrayList<User> readFromFile(String location) {
		ArrayList<User> users = new ArrayList<User>();
		try {
			FileInputStream fis = new FileInputStream(location);
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            users = (ArrayList<User>) ois.readObject();
 
            ois.close();
            fis.close();
			
 
		} catch (Exception e) {
			log.info("error load cache from file " + e.toString());
		}
 
		log.info("Data loaded successfully from file " + location);
		for(User u : users){
			log.info("Username: " + u.getUsername());
			log.info("Password: " + u.getPassword());
		}

		return users;
	}
}
