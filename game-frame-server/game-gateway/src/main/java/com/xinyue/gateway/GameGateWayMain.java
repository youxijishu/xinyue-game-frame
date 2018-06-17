package com.xinyue.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.xinyue.gateway.server.GameGatewayServer;

@SpringBootApplication
public class GameGateWayMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameGateWayMain.class);
		app.setWebEnvironment(false);
		ApplicationContext applicationContext = app.run(args);
		GameGatewayServer gameGatewayServer = applicationContext.getBean(GameGatewayServer.class);
		gameGatewayServer.startServer();
	}
}
