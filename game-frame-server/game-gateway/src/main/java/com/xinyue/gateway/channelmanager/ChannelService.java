package com.xinyue.gateway.channelmanager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.xinyue.network.message.common.IGameMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

/**
 * 此类负责管理角色id和channel的对应关系，并且负责给某个角色id的channel发送消息。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月10日 上午12:03:38
 */
@Service
public class ChannelService {
	private EventLoopGroup loopGroup;
	private Map<Integer, GameChannelGroup> eventLoopMap = new HashMap<>();
	private boolean hadInit;
	private AtomicInteger channelCount = new AtomicInteger();

	/**
	 * 
	 * @Desc 获取当前在线人数
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午3:58:20
	 *
	 */
	public int getOnlineCount() {
		return this.channelCount.get();
	}

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

	private int getIndex(Long roleId) {
		int index = (int) (roleId % eventLoopMap.size());
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

	public void addChannel(Long roleId, Channel channel) {
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(roleId);
		gameChannelGroup.addChannel(roleId, channel);
		this.channelCount.incrementAndGet();
	}

	public void removeChannel(Long roleId, ChannelId channelId) {
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(roleId);
		if (gameChannelGroup != null) {
			gameChannelGroup.removeChannel(roleId, channelId);
			int result = this.channelCount.decrementAndGet();
			if (result < 0) {
				this.channelCount.set(0);
			}
		}
	}

	public void writeMessage(IGameMessage message) {
		long roleId = message.getMessageHead().getRoleId();
		GameChannelGroup gameChannelGroup = this.getGameChannelGroup(roleId);
		if (gameChannelGroup != null) {
			gameChannelGroup.writeMessage(roleId, message);
		}
	}

}
