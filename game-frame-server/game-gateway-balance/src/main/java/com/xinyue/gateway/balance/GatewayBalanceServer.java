package com.xinyue.gateway.balance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayBalanceServer {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GatewayBalanceServer.class);
		app.setWebEnvironment(true);
		app.run(args);
		
	}
}
