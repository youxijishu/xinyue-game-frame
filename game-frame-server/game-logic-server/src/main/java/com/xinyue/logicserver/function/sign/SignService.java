package com.xinyue.logicserver.function.sign;

import com.xinyue.network.message.impl.SignRequest;
import com.xinyue.network.message.impl.SignResponse;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelHandlerContext;
import com.xinyue.rocketmq.framework.messagehandler.MessageHandler;

@MessageHandler
public class SignService {
	@MessageHandler
	public void sign(SignRequest request, GameChannelHandlerContext ctx) {
		System.out.println("收到签到请求：roleId:" + request.getRoleId());
		SignResponse response = request.newResponse();
		response.setResult(true);
		ctx.writeMessage(response);
	}
	
}
