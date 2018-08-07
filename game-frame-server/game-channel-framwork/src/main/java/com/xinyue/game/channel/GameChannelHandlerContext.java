package com.xinyue.game.channel;

import com.xinyue.network.message.common.IGameMessage;

import io.netty.util.concurrent.EventExecutor;

public interface GameChannelHandlerContext extends GameChannelInboundInvoker, GameChannelOutboundInvoker {

	GameChannel channel();
	
	String getName();

	EventExecutor executor();

	GameChannelHandler handler();

	@Override
	GameChannelHandlerContext fireReadMessage(IGameMessage message);

	@Override
	GameChannelHandlerContext fireExceptionCaught(Throwable cause);

	@Override
	GameChannelHandlerContext  fireUserEventTriggered(Object event);

	GameChannelPipeline pipeline();
}
