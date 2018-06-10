package com.xinyue.network.message.common;

public class MessageHead {
	// 在与客户端传输中需要序列化的字段
	private int messageId;
	// 消息序列号
	private int seqId;

	// 错误码，只有服务器返回给客户端的消息中才会有。
	private int errorCode;
	
	
	// ----以下字段服务器内部使用的字段，不需要在传输中序列化的字段---
	
	private String errorMsg;
	private long roleId;
	// 用户id
	private long userId;
	private GameMessageType gameMessageType;
	
	

	public GameMessageType getGameMessageType() {
		return gameMessageType;
	}

	public void setGameMessageType(GameMessageType gameMessageType) {
		this.gameMessageType = gameMessageType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
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

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

}
