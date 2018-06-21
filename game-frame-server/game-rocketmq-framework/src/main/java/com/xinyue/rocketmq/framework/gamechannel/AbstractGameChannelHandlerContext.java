package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public abstract class AbstractGameChannelHandlerContext implements GameChannelHandlerContext {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(AbstractGameChannelHandlerContext.class);

	volatile AbstractGameChannelHandlerContext prev;
	volatile AbstractGameChannelHandlerContext next;

	private final DefaultGameChannelPipeline pipeline;
	private boolean inbound;
	private boolean outbound;

	private String name;

	AbstractGameChannelHandlerContext(DefaultGameChannelPipeline pipeline, String name, boolean inbound,
			boolean outbound) {

		this.pipeline = pipeline;
		this.inbound = inbound;
		this.outbound = outbound;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public GameChannel channel() {
		return pipeline.channel();
	}

	@Override
	public EventExecutor executor() {
		return channel().executor();
	}

	@Override
	public GameChannelPipeline pipeline() {
		return pipeline;
	}

	@Override
	public void close() {
		final AbstractGameChannelHandlerContext next = findContextOutbound();
		EventExecutor executor = next.executor();
		if (executor.inEventLoop()) {
			next.invokeClose();
		} else {
			executor.execute(() -> {
				next.invokeClose();
			});
		}
	}

	private void invokeClose() {
		try {
			((GameChannelOutboundHandler) handler()).close(this);
		} catch (Throwable t) {
			logger.error("关闭channel时错误", t);
		}
	}

	@Override
	public GameChannelHandlerContext fireExceptionCaught(Throwable cause) {
		invokeExceptionCaught(findContextInbound(), cause);
		return this;
	}

	static void invokeExceptionCaught(final AbstractGameChannelHandlerContext next, final Throwable cause) {
		ObjectUtil.checkNotNull(cause, "cause");
		EventExecutor executor = next.executor();
		if (executor.inEventLoop()) {
			next.invokeExceptionCaught(cause);
		} else {
			try {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						next.invokeExceptionCaught(cause);
					}
				});
			} catch (Throwable t) {
				if (logger.isWarnEnabled()) {
					logger.warn("Failed to submit an exceptionCaught() event.", t);
					logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
				}
			}
		}
	}

	@Override
	public GameChannelHandlerContext fireUserEventTriggered(Object event) {
		invokeUserEventTriggered(findContextInbound(), event);
		return this;
	}

	static void invokeUserEventTriggered(final AbstractGameChannelHandlerContext next, final Object event) {
		ObjectUtil.checkNotNull(event, "event");
		EventExecutor executor = next.executor();
		if (executor.inEventLoop()) {
			next.invokeUserEventTriggered(event);
		} else {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					next.invokeUserEventTriggered(event);
				}
			});
		}
	}

	private void invokeUserEventTriggered(Object event) {
		try {
			((GameChannelInboundHandler) handler()).userEventTriggered(this, event);
		} catch (Throwable t) {
			notifyHandlerException(t);
		}
	}

	private void notifyHandlerException(Throwable cause) {
		if (inExceptionCaught(cause)) {
			if (logger.isWarnEnabled()) {
				logger.warn("An exception was thrown by a user handler " + "while handling an exceptionCaught event",
						cause);
			}
			return;
		}

		invokeExceptionCaught(cause);
	}

	private void invokeExceptionCaught(final Throwable cause) {
		try {
			((GameChannelInboundHandler) handler()).exceptionCaught(this, cause);
		} catch (Throwable error) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"An exception {}" + "was thrown by a user handler's exceptionCaught() "
								+ "method while handling the following exception:",
						ThrowableUtil.stackTraceToString(error), cause);
			} else if (logger.isWarnEnabled()) {
				logger.warn("An exception '{}' [enable DEBUG level for full stacktrace] "
						+ "was thrown by a user handler's exceptionCaught() "
						+ "method while handling the following exception:", error, cause);
			}
		}
	}

	private static boolean inExceptionCaught(Throwable cause) {
		do {
			StackTraceElement[] trace = cause.getStackTrace();
			if (trace != null) {
				for (StackTraceElement t : trace) {
					if (t == null) {
						break;
					}
					if ("exceptionCaught".equals(t.getMethodName())) {
						return true;
					}
				}
			}

			cause = cause.getCause();
		} while (cause != null);

		return false;
	}

	@Override
	public GameChannelHandlerContext fireReadMessage(IGameMessage message) {

		invokeReadMessage(findContextInbound(), message);
		return this;
	}

	static void invokeReadMessage(final AbstractGameChannelHandlerContext next, IGameMessage msg) {
		EventExecutor executor = next.executor();
		if (executor.inEventLoop()) {
			next.invokeChannelRead(msg);
		} else {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					next.invokeChannelRead(msg);
				}
			});
		}
	}

	private void invokeChannelRead(IGameMessage msg) {
		try {
			((GameChannelInboundHandler) handler()).readMessage(this, msg);
		} catch (Throwable t) {
			notifyHandlerException(t);
		}
	}

	@Override
	public void writeMessage(IGameMessage message) {
		invokeWriteMessage(findContextOutbound(), message);
	}

	static void invokeWriteMessage(final AbstractGameChannelHandlerContext next, IGameMessage msg) {
		EventExecutor executor = next.executor();
		if (executor.inEventLoop()) {
			next.invokerWriteMessage(msg);
		} else {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					next.invokerWriteMessage(msg);
				}
			});
		}
	}

	private void invokerWriteMessage(IGameMessage gameMessage) {
		((GameChannelOutboundHandler) handler()).writeMessage(this, gameMessage);
	}

	private AbstractGameChannelHandlerContext findContextInbound() {
		AbstractGameChannelHandlerContext ctx = this;
		do {
			ctx = ctx.next;
		} while (!ctx.inbound);
		return ctx;
	}

	private AbstractGameChannelHandlerContext findContextOutbound() {
		AbstractGameChannelHandlerContext ctx = this;
		do {
			ctx = ctx.prev;
		} while (!ctx.outbound);
		return ctx;
	}

}
