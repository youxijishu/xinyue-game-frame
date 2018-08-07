package com.xinyue.game.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinyue.concurrent.GameTask;
import com.xinyue.game.channel.events.IdleEvent;
import com.xinyue.network.message.common.IGameMessage;

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
	private Map<Long, GameChannelInfo> gameChannelMap = new HashMap<>();
	private EventExecutor executor;
	private IGameChannelInit channelInit;
	private int channelMaxIdleTime = 900;

	public GameChannelGroup(EventExecutor executor, int channelMaxIdelTime, IGameChannelInit channelInit) {
		this.executor = executor;
		this.channelInit = channelInit;
		if (channelMaxIdelTime != 0) {
			this.channelMaxIdleTime = channelMaxIdelTime;
		}

	}

	private GameChannel getGameChannel(long channelId) {
		GameChannelInfo gameChannelInfo = this.gameChannelMap.get(channelId);
		if (gameChannelInfo == null) {
			GameChannel gameChannel = new DefaultGameChannel(this, channelId);
			channelInit.init(gameChannel);
			gameChannelInfo = new GameChannelInfo();
			gameChannelInfo.setGameChannel(gameChannel);
			this.gameChannelMap.put(channelId, gameChannelInfo);
			this.checkTimeOut(channelId);
		}
		gameChannelInfo.setLastUserTime(System.currentTimeMillis());
		return gameChannelInfo.getGameChannel();
	}

	/**
	 * 
	 * @Desc 检测channel的空闲时间，如果达到这个空闲时间了，则移除gamechannel，并发送一个空闲事件
	 * @param channelId
	 * @Author 心悦网络 王广帅
	 * @Date 2018年7月18日 下午11:56:25
	 *
	 */
	private void checkTimeOut(long channelId) {
		this.executor.schedule(() -> {
			GameChannelInfo gameChannelInfo = this.gameChannelMap.get(channelId);
			if (gameChannelInfo != null) {
				long maxIdle = channelMaxIdleTime * 1000;
				if (System.currentTimeMillis() - gameChannelInfo.getLastUserTime() > maxIdle) {
					gameChannelInfo.getGameChannel().readEvent(new IdleEvent());
					closeChannel(channelId);
				} else {
					this.checkTimeOut(channelId);
				}
			}
		}, channelMaxIdleTime, TimeUnit.SECONDS);
	}

	private void execute(Runnable task) {
		this.executor.execute(task);
	}

	public void closeChannel(long roleId) {
		if (this.executor.inEventLoop()) {
			gameChannelMap.remove(roleId);
		} else {
			this.execute(new GameTask() {

				@Override
				public void doRun() {
					gameChannelMap.remove(roleId);
				}
			});
		}
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
		if (this.executor.inEventLoop()) {
			long roleId = gameMessage.getMessageHead().getRoleId();
			GameChannel gameChannel = getGameChannel(roleId);
			gameChannel.readMessage(gameMessage);

		} else {
			this.execute(new GameTask() {

				@Override
				public void doRun() {
					long roleId = gameMessage.getMessageHead().getRoleId();
					GameChannel gameChannel = getGameChannel(roleId);
					gameChannel.readMessage(gameMessage);

				}
			});
		}

	}

	public void fireReadEvent(Object event, long roleId) {
		if (this.executor.inEventLoop()) {
			GameChannel gameChannel = getGameChannel(roleId);
			gameChannel.readEvent(event);
		} else {
			this.execute(new GameTask() {

				@Override
				public void doRun() {
					GameChannel gameChannel = getGameChannel(roleId);
					gameChannel.readEvent(event);
				}
			});
		}
	}

	private class GameChannelInfo {
		private GameChannel gameChannel;
		private long lastUserTime;

		public GameChannel getGameChannel() {
			return gameChannel;
		}

		public void setGameChannel(GameChannel gameChannel) {
			this.gameChannel = gameChannel;
		}

		public long getLastUserTime() {
			return lastUserTime;
		}

		public void setLastUserTime(long lastUserTime) {
			this.lastUserTime = lastUserTime;
		}

	}
}
