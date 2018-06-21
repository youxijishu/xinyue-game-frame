package com.xinyue.rocketmq.framework;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.xinyue.network.EnumServerType;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.network.message.inner.InnerMessageHeader;
import com.xinyue.rocketmq.RocketmqConfig;
import com.xinyue.rocketmq.framework.gamechannel.GameChannel;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelGroupManager;
import com.xinyue.rocketmq.framework.gamechannel.IGameChannelInit;
import com.xinyue.rocketmq.framework.messagehandler.GameMessageMethodInvokerMapping;
import com.xinyue.rocketmq.framework.network.GameMessageRouter;
import com.xinyue.rocketmq.framework.network.LogicMessageConsumerListener;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class GameApplication {

	private GameChannelGroupManager gameChannelGroupManager;
	private int threads;
	private EventExecutorGroup eventExecutorGroup;
	private ApplicationContext applicationContext;
	private String[] gameMessageConsumerTags;
	private GameMessageRouter gameMessageRouter;
	private GameMessageMethodInvokerMapping gameMessageMethodInvokerMapping;
	private int serverId;

	public void run(int serverId, int threads, ApplicationContext applicationContext) throws Exception {
		this.threads = threads;
		this.serverId = serverId;
		this.applicationContext = applicationContext;
		gameMessageMethodInvokerMapping = new GameMessageMethodInvokerMapping(applicationContext);
		gameMessageMethodInvokerMapping.scanGameMessageMapping();
		this.findGameMessageConsumerTags();
		RocketmqConfig rocketmqConfig = applicationContext.getBean(RocketmqConfig.class);
		this.eventExecutorGroup = new DefaultEventExecutorGroup(threads);
		gameChannelGroupManager = new GameChannelGroupManager();
		gameMessageRouter = new GameMessageRouter();
		LogicMessageConsumerListener messageListener = new LogicMessageConsumerListener(gameChannelGroupManager,this.gameMessageMethodInvokerMapping.getAllGameMessageClass());
		gameMessageRouter.init(rocketmqConfig, messageListener, gameMessageConsumerTags);
		IGameChannelInit gameChannelInit = new IGameChannelInit() {

			@Override
			public void init(GameChannel channel) {
				
			}
		};
		gameChannelGroupManager.init(gameMessageRouter, eventExecutorGroup, gameChannelInit);
	}

	private void findGameMessageConsumerTags() {
		List<Class<? extends IGameMessage>> gameMessageClasses = this.gameMessageMethodInvokerMapping
				.getAllGameMessageClass();
		gameMessageConsumerTags = new String[gameMessageClasses.size()];
		for (int i = 0; i < gameMessageClasses.size(); i++) {
			GameMessageMetaData messageMetaData = gameMessageClasses.get(i).getAnnotation(GameMessageMetaData.class);
			short messageId = messageMetaData.id();
			EnumServerType serverType = messageMetaData.serverType();
			gameMessageConsumerTags[i] = InnerMessageHeader.getLogicServerMessageTag(serverType, messageId, serverId);
		}
	}

	public int getThreads() {
		return threads;
	}

	public EventExecutorGroup getEventExecutorGroup() {
		return eventExecutorGroup;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
