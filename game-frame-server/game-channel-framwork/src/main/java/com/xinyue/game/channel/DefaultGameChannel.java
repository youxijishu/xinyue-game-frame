package com.xinyue.game.channel;

import com.xinyue.network.message.common.IGameMessage;

import io.netty.util.concurrent.EventExecutor;

public class DefaultGameChannel implements GameChannel {
	private long channelId;
	private DefaultGameChannelPipeline pipeline;
	private EventExecutor executor;

	public DefaultGameChannel(GameChannelGroup channelGroup, long channelId) {
		this.channelId = channelId;
		this.executor = channelGroup.getExecutor();
		pipeline = newChannelPipeline();
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
	public GameChannelPipeline pipeline() {
		return pipeline;
	}



}
