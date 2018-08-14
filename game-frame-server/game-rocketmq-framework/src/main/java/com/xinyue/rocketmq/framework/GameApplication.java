package com.xinyue.rocketmq.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.xinyue.rocketmq.framework.config.RocketmqServerConfig;
import com.xinyue.rocketmq.framework.gamechannel.GameChannel;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelGroupManager;
import com.xinyue.rocketmq.framework.gamechannel.IGameChannelInit;
import com.xinyue.rocketmq.framework.messagehandler.GameMessageMethodInvokerMapping;
import com.xinyue.rocketmq.framework.messagehandler.LogicServerMessagerHandler;
import com.xinyue.rocketmq.framework.network.LogicServerGameMessageRouter;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

@Service
public class GameApplication {
	@Autowired
	private GameChannelGroupManager gameChannelGroupManager;
	private EventExecutorGroup eventExecutorGroup;
	@Autowired
	private LogicServerGameMessageRouter gameMessageRouter;
	@Autowired
	private GameMessageMethodInvokerMapping gameMessageMethodInvokerMapping;
	private static GameApplication instance = null;
	@Autowired
	private RocketmqServerConfig serverConfig;
	@Autowired
	private ApplicationContext applicationContext;

	public static void start(ApplicationContext applicationContext) throws Exception {
		instance = applicationContext.getBean(GameApplication.class);
		instance.run();
	}

	public void run() throws Exception {

		gameMessageMethodInvokerMapping.scanGameMessageMapping();
		int threads = serverConfig.getThreads();
		this.eventExecutorGroup = new DefaultEventExecutorGroup(threads);
		gameMessageRouter.start();
		IGameChannelInit gameChannelInit = new IGameChannelInit() {

			@Override
			public void init(GameChannel channel) {
				LogicServerMessagerHandler logicServerMessagerHandler = applicationContext
						.getBean(LogicServerMessagerHandler.class);
				channel.pipeline().addLast("gameMessageHandler", logicServerMessagerHandler);
			}
		};
		gameChannelGroupManager.init(gameMessageRouter, eventExecutorGroup, gameChannelInit);
	}

	public EventExecutorGroup getEventExecutorGroup() {
		return eventExecutorGroup;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public RocketmqServerConfig getServerConfig() {
		return serverConfig;
	}

}
