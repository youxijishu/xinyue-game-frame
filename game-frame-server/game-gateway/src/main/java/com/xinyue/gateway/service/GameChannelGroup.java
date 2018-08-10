package com.xinyue.gateway.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinyue.gateway.message.IGateMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.concurrent.EventExecutor;

/**
 * 一个GameChannel的集合，负责管理channel与用户的id的映射。这里使用一个EventExecutor来控制对集合的操作，保证集合所有的操作都在同一个线程中。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月3日 下午9:40:04
 */
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

	public void addChannel(Long roleId, Channel channel) {
		this.execute(() -> {
			channelMap.put(roleId, channel);
		});
	}

	/**
	 * 
	 * @Desc 这里在删除的时候需要保证线程安全，以及检测要删除的channelId和已存在的channelId是否一样。不一样不做删除。
	 *       因为一个用户的连接断开之后，可能又迅速建立了一条新的连接。
	 * @param roleId
	 * @param channelId
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午10:38:47
	 *
	 */
	public void removeChannel(Long roleId, ChannelId channelId) {
		this.execute(() -> {
			Channel channel = channelMap.get(roleId);
			if (channel != null && channel.id().equals(channelId)) {
				channelMap.remove(roleId);
			}
		});
	}

	public void writeMessage(Long roleId, IGateMessage message) {
		this.execute(() -> {
			Channel channel = channelMap.get(roleId);
			if (channel == null) {
				logger.debug("roleId[{}]对应的channel为null", roleId);
			} else {
				if (channel.isActive() && channel.isOpen()) {
					channel.writeAndFlush(message);
				} else {
					logger.debug("roleId[{}]的channel已关闭", roleId);
				}
			}
		});
	}

}
