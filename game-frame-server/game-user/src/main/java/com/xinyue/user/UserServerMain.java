package com.xinyue.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServerMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UserServerMain.class);
		app.setWebEnvironment(true);
		app.run(args);
	}
}
