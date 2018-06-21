package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

public class GameChannelHandlerContextOfHead extends AbstractGameChannelHandlerContext
		implements GameChannelInboundHandler, GameChannelOutboundHandler {

	GameChannelHandlerContextOfHead(DefaultGameChannelPipeline pipeline) {
		super(pipeline, null, false, true);
	}

	@Override
	public GameChannelHandler handler() {
		return this;
	}

	@Override
	public void readMessage(GameChannelHandlerContext ctx, IGameMessage message) {
		ctx.fireReadMessage(message);
	}

	@Override
	public void userEventTriggered(GameChannelHandlerContext ctx, Object event) {
		ctx.fireUserEventTriggered(event);
	}

	@Override
	public void exceptionCaught(GameChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
	
	@Override
	public void writeMessage(GameChannelHandlerContext ctx, IGameMessage gameMessage) {
		pipeline().channel().getUnsafe().writeMessage(gameMessage);
	}

	@Override
	public void close(GameChannelHandlerContext ctx) {
		long channelKey = pipeline().channel().channelId();
		pipeline().channel().getUnsafe().closeChannel(channelKey);
	}

}
