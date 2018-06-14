package com.xinyue.network.message.common;

import io.netty.buffer.ByteBuf;

public class MessageHead {
	// 在与客户端传输中需要序列化的字段
	private int messageId;
	// 消息序列号
	private int seqId;
	private GameMessageType gameMessageType;

	public GameMessageType getGameMessageType() {
		return gameMessageType;
	}
	
	public void readMessage(ByteBuf byteBuf){
		this.seqId = byteBuf.readInt();
		this.messageId = byteBuf.readInt();
	}

	public void setGameMessageType(GameMessageType gameMessageType) {
		this.gameMessageType = gameMessageType;
	}

	

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	

	

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	

}
