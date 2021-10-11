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

    public static void registerUser(User user, String location, ArrayList<User> users) {
		// Add a user to the list of all current users and save it in a file (and close the Output-Streams)
		try {
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
		// Check if the file exists and create it if not
		if (!f.exists()) {
			f.createNewFile();
		  }

		// Read all users (ArrayList<User>) from the file and store the data in a variable
		// (and close the Input-Streams) as well as returning said variable
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

	/*
		In the following there are 3 buildResponse()-Method each for a different usecase:
			1. For the response regarding the users and their statuses
			2. For the response regarding the start of a call (The positive response needs the username extra)
			3. The default response which is used in all other cases
	*/
	
	public static String buildResponse(Map<String, String> User){

		/*
			Create a JSONObject in the following format:
			{
				"Action":"StatusResponse",
				"User":[
					{"Username":"Status"},
					{"Username":"Status"},
					{"Username":"Status"}
				]
			}
		*/

		JsonArrayBuilder users = Json.createArrayBuilder();

		User.forEach((k, v) -> {
			users.add(Json.createObjectBuilder().add(k,v));
		});	

		JsonObject res = Json.createObjectBuilder()
			.add("Action", "StatusResponse")
			.add("User", users)
            .build();


		// Convert the JSONObject to a string as the sendText()-Method expects a string
        Writer writer = new StringWriter();
        Json.createWriter(writer).write(res);
		return writer.toString();

	}

	public static String buildResponse(String Action, String Value, String Username){

		/*
			Create a JSONObject in the following format:
			{
				"Action":"<Actionname>",
				"Value":"<Value>",
				"Username":"<Username>"
			}
		*/

		JsonObject res = Json.createObjectBuilder()
            .add("Action", Action)
            .add("Value", Value)
			.add("Username", Username)
            .build();

		// Convert the JSONObject to a string as the sendText()-Method expects a string
        Writer writer = new StringWriter();
        Json.createWriter(writer).write(res);
		return writer.toString();

	}

	public static String buildResponse(String Action, String Value){

		/*
			Create a JSONObject in the following format:
			{
				"Action":"<Actionname>",
				"Value":"<Value>"
			}
		*/

		JsonObject res = Json.createObjectBuilder()
            .add("Action", Action)
            .add("Value", Value)
            .build();

		// Convert the JSONObject to a string as the sendText()-Method expects a string
        Writer writer = new StringWriter();
        Json.createWriter(writer).write(res);
		return writer.toString();

	}
}
