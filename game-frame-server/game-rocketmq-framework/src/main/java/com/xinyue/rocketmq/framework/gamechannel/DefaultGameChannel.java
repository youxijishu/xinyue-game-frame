package com.xinyue.rocketmq.framework.gamechannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinyue.network.message.common.IGameMessage;

import io.netty.util.concurrent.EventExecutor;

public class DefaultGameChannel implements GameChannel {
	private long channelId;
	private DefaultGameChannelPipeline pipeline;
	private EventExecutor executor;
	private volatile boolean isClose;
	private Unsafe unsafe;
	private Logger logger = LoggerFactory.getLogger(DefaultGameChannel.class);

	public DefaultGameChannel(GameChannelGroup channelGroup, long channelId) {
		this.channelId = channelId;
		this.executor = channelGroup.getExecutor();
		pipeline = newChannelPipeline();
		this.unsafe = new channelUnsafe(channelGroup);
	}

	protected DefaultGameChannelPipeline newChannelPipeline() {
		return new DefaultGameChannelPipeline(this);
	}

	@Override
	public long channelId() {
		return channelId;
	}

	@Override
	public EventExecutor executor() {
		return this.executor;
	}

	@Override
	public void readMessage(IGameMessage message) {
		pipeline.fireReadMessage(message);
	}

	@Override
	public void readEvent(Object event) {
		pipeline.fireUserEventTriggered(event);
	}

	@Override
	public void close() {
		this.isClose = true;
		pipeline.close();
	}

	@Override
	public GameChannelPipeline pipeline() {
		return pipeline;
	}

	@Override
	public boolean isClose() {
		return isClose;
	}

	@Override
	public Unsafe getUnsafe() {
		return this.unsafe;
	}

	/**
	 * 这个是框架调用的接口，用户不要手动调用
	 * 
	 * @author 王广帅
	 * @company 心悦网络
	 * @Date 2018年5月15日 下午10:53:21
	 */
	class channelUnsafe implements Unsafe {
		private GameChannelGroup channelGroup;

		public channelUnsafe(GameChannelGroup channelGroup) {
			super();
			this.channelGroup = channelGroup;
		}

		@Override
		public void writeMessage(IGameMessage gameMessage) {
			try {
				channelGroup.sendMessage(gameMessage);
			} catch (Exception e) {
				logger.error("消息发送失败，{}", gameMessage, e);
			}
		}

		@Override
		public void closeChannel(long channelKey) {
			channelGroup.closeChannel(channelKey);
		}

	}

}
