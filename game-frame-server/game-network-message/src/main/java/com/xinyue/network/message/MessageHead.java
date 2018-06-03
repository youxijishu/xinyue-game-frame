package com.xinyue.network.message;

public class MessageHead {
	// 在与客户端传输中需要序列化的字段
	private int messageId;

	// 服务器内部使用的字段，不需要在传输中序列化的字段
	private long userId;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
