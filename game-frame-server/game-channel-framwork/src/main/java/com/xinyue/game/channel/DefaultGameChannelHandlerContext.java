package com.xinyue.game.channel;

public class DefaultGameChannelHandlerContext extends AbstractGameChannelHandlerContext {
	private final GameChannelHandler handler;

	DefaultGameChannelHandlerContext(DefaultGameChannelPipeline pipeline,String name, GameChannelHandler handler) {
		super(pipeline, name,isInbound(handler), isOutbound(handler));
		this.handler = handler;
	}

	@Override
	public GameChannelHandler handler() {
		return handler;
	}

	private static boolean isInbound(GameChannelHandler handler) {
		return handler instanceof GameChannelInboundHandler;
	}

	private static boolean isOutbound(GameChannelHandler handler) {
		return handler instanceof GameChannelOutboundHandler;
	}
}
