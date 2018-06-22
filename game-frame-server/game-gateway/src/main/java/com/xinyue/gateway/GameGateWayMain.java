package com.xinyue.gateway;

import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.xinyue.gateway.server.GameGatewayServer;
import com.xinyue.gateway.server.GateGameMessageRouter;

@SpringBootApplication(scanBasePackages = { "com.xinyue.gateway", "com.xinyue.rocketmq" })
public class GameGateWayMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameGateWayMain.class);
		app.setWebEnvironment(false);
		ApplicationContext applicationContext = app.run(args);

		GateGameMessageRouter gameMessageRouter = applicationContext.getBean(GateGameMessageRouter.class);
		try {
			gameMessageRouter.start();
		} catch (MQClientException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GameGatewayServer gameGatewayServer = applicationContext.getBean(GameGatewayServer.class);
		gameGatewayServer.startServer();
	}
}
