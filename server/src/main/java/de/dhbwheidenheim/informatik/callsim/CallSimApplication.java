package de.dhbwheidenheim.informatik.callsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.dhbwheidenheim.informatik.callsim.controller.SessionHandler;

@SpringBootApplication(exclude = {
	org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class CallSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallSimApplication.class, args);
		SessionHandler.setLogger();
	}
}
