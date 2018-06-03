package com.xinyue.gateway.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xinyue.network.message.IGameMessage;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

@Service
public class ChannelService {
	private EventLoopGroup loopGroup;
	private Map<Integer, GameChannelGroup> eventLoopMap = new HashMap<>();
	private boolean hadInit;

	/**
	 * 
	 * @Desc 初始化，初始化的时候，会根据线程组的数量对ChannelGroup的管理进行分组。这样可以保证在使用的时候只有读取操作，避免多线程读写eventLoopMap集合。
	 * @param loopGroup
	 * @Author 河南心悦网络科技有限公司 王广帅
	 * @Date 2018年6月3日 下午9:49:30
	 *
	 */
	public void init(EventLoopGroup loopGroup) {
		if (hadInit) {
			throw new UnsupportedOperationException("不可以重复初始化");
		}
		this.hadInit = true;
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
		GameChannelGroup gameChannelGroup = eventLoopMap.get(index);
		if (gameChannelGroup == null) {
			gameChannelGroup = new GameChannelGroup(loopGroup.next());
			eventLoopMap.put(index, gameChannelGroup);
		}
		return gameChannelGroup;
	}

	public void addChannel(Long userId, Channel channel) {
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(userId);
		gameChannelGroup.addChannel(userId, channel);
	}

	public void removeChannel(Long userId) {
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(userId);
		if (gameChannelGroup != null) {
			gameChannelGroup.removeChannel(userId);
		}
	}

	public void writeMessage(IGameMessage message) {
		long userId = message.getMessageHead().getUserId();
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(userId);
		if (gameChannelGroup != null) {
			gameChannelGroup.writeMessage(userId, message);
		}
	}

}
