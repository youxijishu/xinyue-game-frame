package com.xinyue.gateway.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;

public class GameChannelGroup {
	private static Logger logger = LoggerFactory.getLogger(GameChannelGroup.class);
	/**
	 * 缓存用户id和channel的映射
	 */
	private Map<Long, Channel> channelMap = new HashMap<>();
	private EventExecutor executor;

	public GameChannelGroup(EventExecutor executor) {
		this.executor = executor;
	}

	private void execute(Runnable task) {
		this.executor.execute(task);
	}

	public void addChannel(Long userId, Channel channel) {
		this.execute(() -> {
			channelMap.put(userId, channel);
		});
	}

	public void removeChannel(Long userId) {
		this.execute(() -> {
			channelMap.remove(userId);
		});
	}

	public void writeMessage(Long userId, Object message) {
		this.execute(() -> {
			Channel channel = channelMap.get(userId);
			if (channel == null) {
				logger.debug("userId[{}]对应的channel为null", userId);
			} else {
				if (channel.isActive() && channel.isOpen()) {
					channel.writeAndFlush(message);
				} else {
					logger.debug("userId[{}]的channel已关闭", userId);
				}
			}
		});
	}

}
