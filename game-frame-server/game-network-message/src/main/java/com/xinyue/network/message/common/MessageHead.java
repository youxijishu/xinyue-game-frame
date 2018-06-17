package com.xinyue.network.message.common;

import com.xinyue.network.EnumServerType;

public class MessageHead {
	// 在与客户端传输中需要序列化的字段
	private short messageId;
	// 消息序列号
	private int seqId;
	private EnumServerType serverType;
	private GameMessageType gameMessageType;

	public EnumServerType getServerType() {
		return serverType;
	}

	public void setServerType(EnumServerType serverType) {
		this.serverType = serverType;
	}

	public GameMessageType getGameMessageType() {
		return gameMessageType;
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

	public short getMessageId() {
		return messageId;
	}

	public void setMessageId(short messageId) {
		this.messageId = messageId;
	}

}
