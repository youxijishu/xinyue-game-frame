package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

public interface GameChannelOutboundHandler extends GameChannelHandler {

	void writeMessage(GameChannelHandlerContext ctx, IGameMessage message);

	void close(GameChannelHandlerContext ctx);
}
