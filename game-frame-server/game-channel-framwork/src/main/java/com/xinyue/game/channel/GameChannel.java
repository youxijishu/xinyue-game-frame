package com.xinyue.game.channel;

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
	 * @Desc channelId就是角色id
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
	
	

}
