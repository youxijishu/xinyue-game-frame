package com.xinyue.rocketmq.framework;

import org.springframework.stereotype.Service;

import com.xinyue.rocketmq.GameMessageRouter;

import io.netty.buffer.ByteBuf;

@Service
public class LogicMessageRouter extends GameMessageRouter {

	@Override
	public void receive(ByteBuf byteBuf) {

	}
}
