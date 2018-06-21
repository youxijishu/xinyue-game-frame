package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

public class DefaultGameChannelPipeline implements GameChannelPipeline {
	private GameChannel gameChannel;
	private AbstractGameChannelHandlerContext head;
	private AbstractGameChannelHandlerContext tail;

	public DefaultGameChannelPipeline(GameChannel gameChannel) {
		this.gameChannel = gameChannel;
		tail = new GameChannelHandlerContextOfTail(this);
		head = new GameChannelHandlerContextOfHead(this);

		head.next = tail;
		tail.prev = head;

	}

	private AbstractGameChannelHandlerContext newContext(String name, GameChannelHandler handler) {
		return new DefaultGameChannelHandlerContext(this, name, handler);
	}

	@Override
	public GameChannel channel() {
		return gameChannel;
	}

	@Override
	public final GameChannelPipeline fireReadMessage(IGameMessage message) {
		AbstractGameChannelHandlerContext.invokeReadMessage(head, message);
		return this;
	}

	@Override
	public GameChannelPipeline fireExceptionCaught(Throwable cause) {
		AbstractGameChannelHandlerContext.invokeExceptionCaught(head, cause);
		return this;
	}

	@Override
	public GameChannelPipeline fireUserEventTriggered(Object event) {
		AbstractGameChannelHandlerContext.invokeUserEventTriggered(head, event);
		return this;
	}

	@Override
	public void writeMessage(IGameMessage message) {
		tail.writeMessage(message);
	}

	@Override
	public GameChannelPipeline addFirst(String name, GameChannelHandler handler) {
		final AbstractGameChannelHandlerContext newCtx;
		synchronized (this) {
			newCtx = newContext(name, handler);
			addFirst0(newCtx);
		}
		return this;
	}

	private void addFirst0(AbstractGameChannelHandlerContext newCtx) {
		AbstractGameChannelHandlerContext nextCtx = head.next;
		newCtx.prev = head;
		newCtx.next = nextCtx;
		head.next = newCtx;
		nextCtx.prev = newCtx;
	}

	@Override
	public GameChannelPipeline addLast(String name, GameChannelHandler handler) {
		final AbstractGameChannelHandlerContext newCtx;
		synchronized (this) {
			newCtx = newContext(name, handler);
			addLast0(newCtx);
		}
		return this;
	}

	private void addLast0(AbstractGameChannelHandlerContext newCtx) {
		AbstractGameChannelHandlerContext prev = tail.prev;
		newCtx.prev = prev;
		newCtx.next = tail;
		
		
		prev.next = newCtx;
		tail.prev = newCtx;
	}

	@Override
	public GameChannelHandler first() {
		GameChannelHandlerContext first = firtContext();
		if (first == null) {
			return null;
		}
		return first.handler();
	}

	@Override
	public GameChannelHandlerContext firtContext() {
		AbstractGameChannelHandlerContext first = head.next;
		if (first == tail) {
			return null;
		}
		return head.next;
	}

	@Override
	public GameChannelHandlerContext lastContext() {
		AbstractGameChannelHandlerContext last = tail.prev;
		if (last == head) {
			return null;
		}
		return last;
	}

	@Override
	public GameChannelHandler last() {
		AbstractGameChannelHandlerContext last = tail.prev;
		if (last == head) {
			return null;
		}
		return last.handler();
	}

	@Override
	public void close() {
		tail.close();
	}

}
