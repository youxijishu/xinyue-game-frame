package com.xinyue.gateway;

import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.xinyue.gateway.server.GameGatewayServer;
import com.xinyue.gateway.service.MessageRouterService;
import com.xinyue.rocketmq.IMessageRouterService;

@SpringBootApplication(scanBasePackages = { "com.xinyue.gateway", "com.xinyue.rocketmq" })
public class GameGateWayMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameGateWayMain.class);
		app.setWebEnvironment(false);
		ApplicationContext applicationContext = app.run(args);

		IMessageRouterService messageRouterService = applicationContext.getBean(MessageRouterService.class);
		try {
			messageRouterService.start();
		} catch (MQClientException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GameGatewayServer gameGatewayServer = applicationContext.getBean(GameGatewayServer.class);
		gameGatewayServer.startServer();
	}
}
