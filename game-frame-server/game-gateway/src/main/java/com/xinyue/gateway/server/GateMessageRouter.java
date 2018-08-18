package com.xinyue.gateway.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.message.GateMessage;
import com.xinyue.gateway.message.GateMessageHeader;
import com.xinyue.gateway.service.ChannelService;
import com.xinyue.network.message.InnerMessageCodecFactory;
import com.xinyue.network.message.common.GameMessageHead;
import com.xinyue.rocketmq.GameMessageRouter;

import io.netty.buffer.ByteBuf;

@Service
public class GateMessageRouter extends GameMessageRouter {

	@Autowired
	private ChannelService channelService;

	@Override
	public void receive(ByteBuf byteBuf) {
		GameMessageHead gameMessageHead = InnerMessageCodecFactory.getInstance().readGameMessageHead(byteBuf);
		GateMessageHeader gateMessageHeader = new GateMessageHeader();
		gateMessageHeader.setErrorCode(gameMessageHead.getErrorCode());
		gateMessageHeader.setMessageId(gameMessageHead.getMessageId());
		gateMessageHeader.setReceiveTime(gameMessageHead.getSendTime());
		gateMessageHeader.setRoleId(gameMessageHead.getRoleId());
		gateMessageHeader.setUserId(gameMessageHead.getUserId());
		gateMessageHeader.setSeqId(gameMessageHead.getSeqId());
		gateMessageHeader.setServerType(gameMessageHead.getServerType());
		GateMessage gateMessage = new GateMessage();
		gateMessage.setHeader(gateMessageHeader);
		gateMessage.setBody(byteBuf);
		channelService.sendMessageToClient(gateMessage);
	}
}
