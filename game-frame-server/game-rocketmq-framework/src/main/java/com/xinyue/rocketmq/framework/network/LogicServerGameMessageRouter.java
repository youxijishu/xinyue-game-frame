package com.xinyue.rocketmq.framework.network;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.network.EnumServerType;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.network.message.inner.InnerMessageCodecFactory;
import com.xinyue.network.message.inner.InnerMessageHeader;
import com.xinyue.rocketmq.GameMessageRouter;
import com.xinyue.rocketmq.RocketmqConfig;
import com.xinyue.rocketmq.framework.config.ServerConfig;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelGroupManager;
import com.xinyue.rocketmq.framework.messagehandler.GameMessageMethodInvokerMapping;

@Service
public class LogicServerGameMessageRouter extends GameMessageRouter {
	@Autowired
	private GameChannelGroupManager gameChannelGroupManager;
	@Autowired
	private RocketmqConfig rocketmqConfig;
	@Autowired
	private ServerConfig serverConfig;
	@Autowired
	private GameMessageMethodInvokerMapping gameMessageMethodInvokerMapping;

	public void sendMessage(IGameMessage gameMessage) throws Exception {
		byte[] body = InnerMessageCodecFactory.getInstance().encode(gameMessage);
		String tag = gameMessage.getMessageHead().getToGateServerMessageTag();
		this.sendMessage(body, tag);
	}

	@Override
	public String[] getAllListenerTags() {
		List<Class<? extends IGameMessage>> gameMessageClasses = gameMessageMethodInvokerMapping
				.getAllGameMessageClass();
		int serverId = serverConfig.getServerId();
		String[] allTags = new String[gameMessageClasses.size()];
		for (int i = 0; i < gameMessageClasses.size(); i++) {
			GameMessageMetaData messageMetaData = gameMessageClasses.get(i).getAnnotation(GameMessageMetaData.class);
			short messageId = messageMetaData.id();
			EnumServerType serverType = messageMetaData.serverType();
			allTags[i] = InnerMessageHeader.getLogicServerMessageTag(serverType, messageId, serverId);
		}
		return allTags;
	}

	@Override
	public RocketmqConfig getRocketmqConfig() {
		return rocketmqConfig;
	}

	@Override
	public MessageListenerConcurrently getMessageListener() {
		List<Class<? extends IGameMessage>> gameMessageClasses = gameMessageMethodInvokerMapping
				.getAllGameMessageClass();
		return new LogicMessageConsumerListener(gameChannelGroupManager, gameMessageClasses);
	}
}
