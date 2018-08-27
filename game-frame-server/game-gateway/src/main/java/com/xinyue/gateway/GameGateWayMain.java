package com.xinyue.gateway;

import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import com.xinyue.gateway.server.GameGatewayServer;
import com.xinyue.gateway.server.GateMessageRouter;
import com.xinyue.gateway.service.ChannelService;

@SpringBootApplication(scanBasePackages = { "com.xinyue" })
@EnableDiscoveryClient
public class GameGateWayMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameGateWayMain.class);
		app.setWebEnvironment(false);
		ApplicationContext applicationContext = app.run(args);

		GameGatewayServer gameGatewayServer = applicationContext.getBean(GameGatewayServer.class);
		GateMessageRouter gateMessageRouter = applicationContext.getBean(GateMessageRouter.class);
		try {
			gateMessageRouter.start();
			ChannelService channelService = applicationContext.getBean(ChannelService.class);
			channelService.init(gameGatewayServer.getWorkerGroup());
			gameGatewayServer.startServer();
		} catch (MQClientException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}
