package com.xinyue.gateway.server.model;

import com.xinyue.network.message.common.MessageHead;

/**
 * 网关接收客户端消息的信息
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午3:12:22
 */
public class GateMessageInfo {
	private long userId;
	private long roleId;
	private String token;

	private MessageHead messageHead;

	private byte[] body;

	public GateMessageInfo(MessageHead messageHead, byte[] body) {
		super();
		this.messageHead = messageHead;
		this.body = body;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public MessageHead getMessageHead() {
		return messageHead;
	}

	public byte[] getBody() {
		return body;
	}

}
