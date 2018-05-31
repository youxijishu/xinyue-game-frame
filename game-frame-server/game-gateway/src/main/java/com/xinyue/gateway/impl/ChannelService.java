package com.xinyue.gateway.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

@Service
public class ChannelService {
	private EventLoopGroup loopGroup;
	private Map<Integer, GameChannelGroup> eventLoopMap = new HashMap<>();

	public void init(EventLoopGroup loopGroup) {
		this.loopGroup = loopGroup;
		int index = 0;
		for (Iterator<EventExecutor> ite = loopGroup.iterator(); ite.hasNext();) {
			EventExecutor executor = ite.next();
			GameChannelGroup gameChannelGroup = new GameChannelGroup(executor);
			eventLoopMap.put(index++, gameChannelGroup);
		}
	}

	private int getIndex(Long userId) {
		int index = (int) (userId % eventLoopMap.size());
		return index;
	}

	private GameChannelGroup getGameChannelGroup(Long userId) {
		int index = this.getIndex(userId);
		return eventLoopMap.get(index);
	}

	public void addChannel(Long userId, Channel channel) {
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(userId);
		if (gameChannelGroup != null) {
			gameChannelGroup.addChannel(userId, channel);
		}
	}

	public void removeChannel(Long userId) {
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(userId);
		if (gameChannelGroup != null) {
			gameChannelGroup.removeChannel(userId);
		}
	}

	public void writeMessage(Long userId, Object message) {
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(userId);
		if (gameChannelGroup != null) {
			gameChannelGroup.writeMessage(userId, message);
		}
	}

}
