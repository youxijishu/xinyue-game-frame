package com.xinyue.gateway;

import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import com.xinyue.gateway.server.GameGatewayServer;
import com.xinyue.gateway.server.GateGameMessageRouter;
import com.xinyue.gateway.service.ChannelService;

@SpringBootApplication(scanBasePackages = { "com.xinyue" })
@EnableDiscoveryClient
public class GameGateWayMain {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameGateWayMain.class);
		app.setWebEnvironment(false);
		ApplicationContext applicationContext = app.run(args);
		// 测试情况下，先不连接rocketmq，只是为了测试协议的正确性
		GateGameMessageRouter gameMessageRouter = applicationContext.getBean(GateGameMessageRouter.class);
		try {
			gameMessageRouter.start();
		} catch (MQClientException e) {
			e.printStackTrace();
			System.exit(0);
		}
		GameGatewayServer gameGatewayServer = applicationContext.getBean(GameGatewayServer.class);
		ChannelService channelService = applicationContext.getBean(ChannelService.class);
		channelService.init(gameGatewayServer.getWorkerGroup());
		gameGatewayServer.startServer();
	}
}
