package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

public interface GameChannelPipeline extends GameChannelInboundInvoker, GameChannelOutboundInvoker {
	GameChannel channel();

	GameChannelPipeline addFirst(String name, GameChannelHandler handler);

	GameChannelPipeline addLast(String name, GameChannelHandler handler);

	GameChannelHandler first();

	GameChannelHandler last();

	GameChannelHandlerContext firtContext();

	GameChannelHandlerContext lastContext();

	@Override
	GameChannelPipeline fireReadMessage(IGameMessage message);

	@Override
	GameChannelPipeline fireExceptionCaught(Throwable cause);

	@Override
	GameChannelPipeline fireUserEventTriggered(Object event);

}
