package de.dhbwheidenheim.informatik.callsim.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbwheidenheim.informatik.callsim.model.User;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static void registerUser(User user, String location) {
		try {
			ArrayList<User> users = readFromFile(location);
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
		

		return users;
	}

	public static String buildResponse(Collection<String> User){
		//TODO: Send all Users with Statuses here (inCall, Online, Offline)
		JsonArrayBuilder tokens = Json.createArrayBuilder();

		for(String u : User){
			tokens.add(u);
		}

		JsonObject res = Json.createObjectBuilder()
			.add("User", tokens)
            .build();

        Writer writer = new StringWriter();
        Json.createWriter(writer).write(res);
		return writer.toString();

	}

	public static String buildResponse(String Statuscode, String Statusword){

		JsonObject res = Json.createObjectBuilder()
            .add("Statuscode", Statuscode)
            .add("Statusword", Statusword)
            .build();

        Writer writer = new StringWriter();
        Json.createWriter(writer).write(res);
		return writer.toString();

	}
}
