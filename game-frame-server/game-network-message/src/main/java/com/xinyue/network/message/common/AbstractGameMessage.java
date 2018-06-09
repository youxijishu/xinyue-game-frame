package com.xinyue.network.message.common;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import io.netty.buffer.ByteBuf;

public abstract class AbstractGameMessage implements IGameMessage {
	private MessageHead messageHead;

	public AbstractGameMessage() {
		messageHead = new MessageHead();
		messageHead.setMessageId(this.getMessageId());
		messageHead.setGameMessageType(this.getGameMessageType());
	}

	@Override
	public MessageHead getMessageHead() {
		return messageHead;
	}

	protected void copyMessageHead(MessageHead responseMessageHead) {
		if (this.getGameMessageType() == GameMessageType.REQUEST) {
			responseMessageHead.setUserId(this.messageHead.getUserId());
			responseMessageHead.setRoleId(this.messageHead.getRoleId());
			responseMessageHead.setSeqId(this.messageHead.getSeqId());
			responseMessageHead.setMsgVersion(this.messageHead.getMsgVersion());
		} else {
			throw new UnsupportedOperationException("Response 消息不能创建返回消息");
		}
	}

	protected abstract int getMessageId();

	protected abstract GameMessageType getGameMessageType();
}
