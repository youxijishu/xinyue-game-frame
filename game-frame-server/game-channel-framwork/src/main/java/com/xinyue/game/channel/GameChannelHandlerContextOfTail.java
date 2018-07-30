package com.xinyue.game.channel;

import com.xinyue.network.message.common.IGameMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class GameChannelHandlerContextOfTail extends AbstractGameChannelHandlerContext
		implements GameChannelInboundHandler {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(GameChannelHandlerContextOfTail.class);

	GameChannelHandlerContextOfTail(DefaultGameChannelPipeline pipeline) {
		super(pipeline,null, true, false);
	}

	@Override
	public GameChannelHandler handler() {
		return this;
	}

	@Override
	public void readMessage(GameChannelHandlerContext ctx, IGameMessage message) {
		
	}

	@Override
	public void userEventTriggered(GameChannelHandlerContext ctx, Object event) {
		logger.warn("未处理的userEventTriggered：" + event);
	}

	@Override
	public void exceptionCaught(GameChannelHandlerContext ctx, Throwable cause) throws Exception {
		onUnhandledInboundException(cause);
	}

	/**
	 * Called once a {@link Throwable} hit the end of the
	 * {@link ChannelPipeline} without been handled by the user in
	 * {@link ChannelHandler#exceptionCaught(ChannelHandlerContext, Throwable)}.
	 */
	protected void onUnhandledInboundException(Throwable cause) {
		logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. "
				+ "It usually means the last handler in the pipeline did not handle the exception.", cause);
	}

	/**
	 * Called once a message hit the end of the {@link ChannelPipeline} without
	 * been handled by the user in
	 * {@link ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}.
	 * This method is responsible to call
	 * {@link ReferenceCountUtil#release(Object)} on the given msg at some
	 * point.
	 */
	protected void onUnhandledInboundMessage(Object msg) {
		logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. "
				+ "Please check your pipeline configuration.", msg);
	}

}
