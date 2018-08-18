package com.xinyue.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class GameEurekaServer {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(GameEurekaServer.class);
		application.run(args);
	}
}
