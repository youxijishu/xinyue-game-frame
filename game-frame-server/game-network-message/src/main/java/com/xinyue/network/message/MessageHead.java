package com.xinyue.network.message;

public class MessageHead {
	// 在与客户端传输中需要序列化的字段
	private int messageId;
	// 消息序列号
	private int seqId;
	// 消息版本
	private short msgVersion;

	// 错误码，只有服务器返回给客户端的消息中才会有。
	private int errorCode;
	private String errorMsg;
	// 服务器内部使用的字段，不需要在传输中序列化的字段
	private long userId;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public short getMsgVersion() {
		return msgVersion;
	}

	public void setMsgVersion(short msgVersion) {
		this.msgVersion = msgVersion;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

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
