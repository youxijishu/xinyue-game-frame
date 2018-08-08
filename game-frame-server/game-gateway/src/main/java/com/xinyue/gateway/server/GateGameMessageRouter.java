package com.xinyue.gateway.server;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.config.ServerConfig;
import com.xinyue.gateway.service.ChannelService;
import com.xinyue.rocketmq.GameMessageRouter;
import com.xinyue.rocketmq.RocketmqConfig;

@Service
public class GateGameMessageRouter extends GameMessageRouter {
	@Autowired
	private ServerConfig serverConfig;
	@Autowired
	private RocketmqConfig rocketmqConfig;
	@Autowired
	private ChannelService channelService;
	
	@Override
	public String[] getAllListenerTags() {
		String[] tags = new String[1];
		tags[0] = String.valueOf(serverConfig.getServerId());
		return tags;
	}

	@Override
	public RocketmqConfig getRocketmqConfig() {
		return rocketmqConfig;
	}

	@Override
	public MessageListenerConcurrently getMessageListener() {
		return new GateMessageSubscibeListener(channelService);
	}

}
