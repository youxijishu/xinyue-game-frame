package com.xinyue.rocketmq.framework.gamechannel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinyue.concurrent.GameTask;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.rocketmq.framework.network.LogicServerGameMessageRouter;

import io.netty.util.concurrent.EventExecutor;

/**
 * 负责管理用户的channel，在同一个GameChannelGroup中，所有的操作都是在同一个线程中执行的。所以是线程安全的。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月20日 上午9:16:01
 */
public class GameChannelGroup {
	private static Logger logger = LoggerFactory.getLogger(GameChannelGroup.class);
	private Map<Long, GameChannel> gameChannelMap = new HashMap<>();
	private EventExecutor executor;
	private IGameChannelInit channelInit;
	private LogicServerGameMessageRouter gameMessageRouter;

	public GameChannelGroup(LogicServerGameMessageRouter gameMessageRouter, EventExecutor executor, IGameChannelInit channelInit) {
		this.executor = executor;
		this.channelInit = channelInit;
		this.gameMessageRouter = gameMessageRouter;
	}

	private GameChannel getGameChannel(long roleId) {
		GameChannel gameChannel = this.gameChannelMap.get(roleId);
		if (gameChannel == null) {
			gameChannel = new DefaultGameChannel(this, roleId);
			channelInit.init(gameChannel);
			this.gameChannelMap.put(roleId, gameChannel);
			logger.debug("初始化game channel");
		}
		return gameChannel;
	}

	private void execute(Runnable task) {
		this.executor.execute(task);
	}

	public void closeChannel(long roleId) {
		this.execute(new GameTask() {

			@Override
			public void doRun() {
				gameChannelMap.remove(roleId);
			}
		});
	}

	public EventExecutor getExecutor() {
		return executor;
	}

	/**
	 * 
	 * @Desc 向channel中发送消息
	 * @param gameMessage
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月20日 上午9:42:07
	 *
	 */
	public void fireReadMessage(IGameMessage gameMessage) {
		this.execute(new GameTask() {

			@Override
			public void doRun() {
				long roleId = gameMessage.getMessageHead().getRoleId();
				GameChannel gameChannel = getGameChannel(roleId);
				gameChannel.readMessage(gameMessage);

			}
		});

	}

	public void fireReadEvent(Object event, long roleId) {
		this.execute(new GameTask() {

			@Override
			public void doRun() {
				GameChannel gameChannel = getGameChannel(roleId);
				gameChannel.readEvent(event);
			}
		});
	}

	public void sendMessage(IGameMessage gameMessage) {
		try {
			gameMessageRouter.sendMessage(gameMessage);
		} catch (Exception e) {
			logger.error("发送消息失败", e);
		}
	}

}
