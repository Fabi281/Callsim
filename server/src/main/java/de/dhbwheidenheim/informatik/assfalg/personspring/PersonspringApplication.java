package de.dhbwheidenheim.informatik.assfalg.personspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
	org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class PersonspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonspringApplication.class, args);
	}

}
