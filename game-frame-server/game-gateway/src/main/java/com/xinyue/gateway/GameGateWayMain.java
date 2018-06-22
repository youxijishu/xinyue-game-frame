package com.xinyue.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.xinyue.gateway.server.GameGatewayServer;

@SpringBootApplication(scanBasePackages = { "com.xinyue.gateway", "com.xinyue.rocketmq" })
public class GameGateWayMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameGateWayMain.class);
		app.setWebEnvironment(false);
		ApplicationContext applicationContext = app.run(args);
//测试情况下，先不连接rocketmq，只是为了测试协议的正确性
//		GateGameMessageRouter gameMessageRouter = applicationContext.getBean(GateGameMessageRouter.class);
//		try {
//			gameMessageRouter.start();
//		} catch (MQClientException e) {
//			e.printStackTrace();
//			System.exit(0);
//		}

		GameGatewayServer gameGatewayServer = applicationContext.getBean(GameGatewayServer.class);
		gameGatewayServer.startServer();
	}
}
