package com.xinyue.game.channel;

import com.xinyue.network.message.common.IGameMessage;

public interface GameChannelOutboundHandler extends GameChannelHandler {

	void writeMessage(GameChannelHandlerContext ctx, IGameMessage message);

}
