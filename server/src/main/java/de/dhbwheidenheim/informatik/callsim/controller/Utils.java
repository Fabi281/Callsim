package de.dhbwheidenheim.informatik.callsim.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

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
 
	public static ArrayList<User> readFromFile(String location) throws IOException {
		ArrayList<User> users = new ArrayList<User>();
		File f = new File(location);
		if (!f.exists()) {
			f.createNewFile();
		  }
		try {
			FileInputStream fis = new FileInputStream(location);
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            users = (ArrayList<User>) ois.readObject();
 
            ois.close();
            fis.close();
			
 
		} catch (Exception e) {
			if(f.length() == 0){
				log.info("file is empty");
			} else {
				log.info("error load cache from file " + e.toString());
			}
		}

		return users;
	}

	public static String buildResponse(Map<String, String> User){

		JsonArrayBuilder users = Json.createArrayBuilder();

		User.forEach((k, v) -> {
			users.add(Json.createObjectBuilder().add(k,v));
		});	

		JsonObject res = Json.createObjectBuilder()
			.add("Action", "StatusResponse")
			.add("User", users)
            .build();

        Writer writer = new StringWriter();
        Json.createWriter(writer).write(res);
		return writer.toString();

	}

	public static String buildResponse(String Action, String Value){

		JsonObject res = Json.createObjectBuilder()
            .add("Action", Action)
            .add("Value", Value)
            .build();

        Writer writer = new StringWriter();
        Json.createWriter(writer).write(res);
		return writer.toString();

	}
}
