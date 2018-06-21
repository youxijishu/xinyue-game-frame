package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

import io.netty.util.concurrent.EventExecutor;

/**
 * 这个channel是用来处理用户收到的消息的
 * 
 * @author 王广帅
 * @company 心悦网络
 * @Date 2018年5月13日 上午11:33:39
 */
public interface GameChannel {
	/**
	 * 
	 * @Desc channel是否已关闭
	 * @return
	 * @Author 王广帅
	 * @Date 2018年5月15日 下午10:44:07
	 *
	 */
	boolean isClose();

	/**
	 * 
	 * @Desc  channelId就是角色id
	 * @return
	 * @Author 王广帅
	 * @Date 2018年5月13日 上午11:33:31
	 *
	 */
	long channelId();

	EventExecutor executor();

	GameChannelPipeline pipeline();

	void readMessage(IGameMessage message);
	void readEvent(Object event);

	void close();

	Unsafe getUnsafe();

	interface Unsafe {
		void writeMessage(IGameMessage gameMessage);

		void closeChannel(long channelKey);
	}

}
