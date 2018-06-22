package com.xinyue.logicserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.xinyue.rocketmq.framework.GameApplication;

@SpringBootApplication(scanBasePackages = { "com.xinyue" })
public class GameLogicServer {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameLogicServer.class);
		app.setWebEnvironment(false);
		ApplicationContext applicationContext = app.run(args);
		try {
			GameApplication.start(applicationContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
