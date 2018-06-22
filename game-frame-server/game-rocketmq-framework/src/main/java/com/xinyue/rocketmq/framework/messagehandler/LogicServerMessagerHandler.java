package com.xinyue.rocketmq.framework.messagehandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinyue.annotations.PrototypeService;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelHandlerContext;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelInboundHandler;
@PrototypeService
public class LogicServerMessagerHandler implements GameChannelInboundHandler {
	private static Logger logger = LoggerFactory.getLogger(LogicServerMessagerHandler.class);
	@Autowired
	private GameMessageMethodInvokerMapping gameMessageMethodInvokerMapping;

	@Override
	public void readMessage(GameChannelHandlerContext ctx, IGameMessage message) {
		gameMessageMethodInvokerMapping.Invoker(message, ctx);
	}

	@Override
	public void userEventTriggered(GameChannelHandlerContext ctx, Object event) {

	}

	@Override
	public void exceptionCaught(GameChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("逻辑异常", cause);
	}

}
