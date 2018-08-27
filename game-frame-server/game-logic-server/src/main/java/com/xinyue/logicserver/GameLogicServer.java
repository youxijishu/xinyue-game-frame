package com.xinyue.logicserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;

import com.xinyue.rocketmq.framework.GameApplication;

@SpringBootApplication(scanBasePackages = { "com.xinyue" })
@EnableEurekaClient
public class GameLogicServer {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameLogicServer.class);
		app.setWebEnvironment(true);
		try {
			ApplicationContext applicationContext = app.run(args);
			GameApplication.start(applicationContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
