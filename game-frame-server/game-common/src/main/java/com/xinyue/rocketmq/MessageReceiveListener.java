package com.xinyue.rocketmq;

import io.netty.buffer.ByteBuf;

public interface MessageReceiveListener {

	public void receive(ByteBuf byteBuf);
}
