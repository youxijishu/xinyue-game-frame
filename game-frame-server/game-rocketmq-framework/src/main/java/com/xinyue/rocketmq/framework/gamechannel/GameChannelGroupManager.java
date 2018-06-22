package com.xinyue.rocketmq.framework.gamechannel;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.rocketmq.framework.network.LogicServerGameMessageRouter;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 这个类负责管理GameChannelGroup，在这个类创建的时候，会根据当前给的线程组来划分成若干线程。每个线程管理一个GameChannelGroup.当收到用户的消息时，根据roleId进行求余计算，获得一个GameChannelGroup，在同一个GameChannelGroup中，所有的操作都是在同一个线程中执行的。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月20日 下午2:53:04
 */
@Service
public class GameChannelGroupManager {

	private GameChannelGroup[] gameChannelGroups;
	private IGameChannelInit gameChannelInit;
	public void init(LogicServerGameMessageRouter gameMessageRouter, EventExecutorGroup eventExecutorGroup,
			IGameChannelInit gameChannelInit) {
		this.gameChannelInit = gameChannelInit;
		Iterator<EventExecutor> iterator = eventExecutorGroup.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			iterator.next();
		}
		iterator = eventExecutorGroup.iterator();
		gameChannelGroups = new GameChannelGroup[count];
		count = 0;
		while (iterator.hasNext()) {
			gameChannelGroups[count++] = new GameChannelGroup(gameMessageRouter, iterator.next(), this.gameChannelInit);
		}
	}

	/**
	 * 
	 * @Desc 接收外部的消息
	 * @param gameMessage
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月20日 下午2:52:29
	 *
	 */
	public void fireReadMessage(IGameMessage gameMessage) {
		long roleId = gameMessage.getMessageHead().getRoleId();
		int index = (int) (roleId % gameChannelGroups.length);
		GameChannelGroup gameChannelGroup = this.gameChannelGroups[index];
		gameChannelGroup.fireReadMessage(gameMessage);
	}

}
