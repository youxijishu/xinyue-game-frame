package com.xinyue.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameWebMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameWebMain.class);
		app.setWebEnvironment(true);
		app.run(args);
	}
}
