package com.xinyue.gateway.message;

/**
 * 客户端与网关通信的消息包头
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年8月11日 上午1:30:40
 */
public class GateMessageHeader {
	private long userId;
	private long roleId;
	// 消息序列id
	private int seqId;
	// 消息id
	private short messageId;
	private long receiveTime;
	private short errorCode;
	//服务类型
	private short serverType;
	// 是否压缩
	private byte isZip;
	private String ip;
	
	
	
	public short getServerType() {
		return serverType;
	}

	public void setServerType(short serverType) {
		this.serverType = serverType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public short getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(short errorCode) {
		this.errorCode = errorCode;
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

	public byte getIsZip() {
		return isZip;
	}

	public void setIsZip(byte isZip) {
		this.isZip = isZip;
	}

	@Override
	public String toString() {
		return "GateMessageHeader [userId=" + userId + ", roleId=" + roleId + ", seqId=" + seqId + ", messageId="
				+ messageId + ", receiveTime=" + receiveTime + ", errorCode=" + errorCode + ", isZip=" + isZip + ", ip="
				+ ip + "]";
	}

}
