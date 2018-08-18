package com.xinyue.network.message.common;

public class GameMessageHead {

	private long roleId;
	private long userId;
	private int seqId;
	private short messageId;
	private short serverType;
	private long sendTime;
	private short fromeServerId;
	private short toServerId;
	private short errorCode;
	private String errorMsg;
	private String ip;
	private GameMessageType messageType;
	
	

	public GameMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(GameMessageType messageType) {
		this.messageType = messageType;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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

	public short getMessageId() {
		return messageId;
	}

	public void setMessageId(short messageId) {
		this.messageId = messageId;
	}

	public short getServerType() {
		return serverType;
	}

	public void setServerType(short serverType) {
		this.serverType = serverType;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public short getFromeServerId() {
		return fromeServerId;
	}

	public void setFromeServerId(short fromeServerId) {
		this.fromeServerId = fromeServerId;
	}

	public short getToServerId() {
		return toServerId;
	}

	public void setToServerId(short toServerId) {
		this.toServerId = toServerId;
	}

	public void setError(IGameError gameError){
		this.errorCode = gameError.getErrorCode();
		this.errorMsg = gameError.getErrorMsg();
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	

	public short getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(short errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "GameMessageHead [roleId=" + roleId + ", userId=" + userId + ", seqId=" + seqId + ", messageId="
				+ messageId + ", serverType=" + serverType + ", sendTime=" + sendTime + ", fromeServerId="
				+ fromeServerId + ", toServerId=" + toServerId + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg
				+ ", ip=" + ip + ", messageType=" + messageType + "]";
	}

	
}
