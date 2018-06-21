package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

public interface GameChannelInboundHandler extends GameChannelHandler {

	void readMessage(GameChannelHandlerContext ctx, IGameMessage message);

	void userEventTriggered(GameChannelHandlerContext ctx, Object event);

    void exceptionCaught(GameChannelHandlerContext ctx, Throwable cause) throws Exception;

}
