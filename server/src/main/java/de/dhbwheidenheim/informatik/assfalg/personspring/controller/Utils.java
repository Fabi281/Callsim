package de.dhbwheidenheim.informatik.assfalg.personspring.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbwheidenheim.informatik.assfalg.personspring.model.Person;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static void writeToFile(Person person, String location) {
		try {

			ArrayList<Person> personen = new ArrayList<Person>();
			personen.add(person);

			FileOutputStream fos = new FileOutputStream(location);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(personen);
            oos.close();
            fos.close();

			log.info("Company data saved at file location: " + location);
		} catch (IOException e) {
			log.info("Hmm.. Got an error while saving Company data to file " + e.toString());
		}
	}
 
	public static ArrayList<Person> readFromFile(String location) {
		ArrayList<Person> personen = new ArrayList<Person>();
		try {
			FileInputStream fis = new FileInputStream(location);
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            personen = (ArrayList<Person>) ois.readObject();
 
            ois.close();
            fis.close();
			
 
		} catch (Exception e) {
			log.info("error load cache from file " + e.toString());
		}
 
		log.info("\nComapny Data loaded successfully from file " + location);
		for(Person p : personen){
			log.info("\nUsername: " + p.getName());
			log.info("\nPassword: " + p.getPassword());
		}

		return personen;
	}
}
