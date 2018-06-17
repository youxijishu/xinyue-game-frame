package com.xinyue.network.message.inner;

import com.xinyue.network.message.common.MessageHead;

/**
 * 内部消息头，即网关和业务服务数据交互的消息包头
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午2:28:05
 */
public class InnerMessageHeader extends MessageHead {
	// 错误码，只有服务器返回给客户端的消息中才会有。
	private int errorCode;
	private String errorMsg;
	private long roleId;
	// 用户id
	private long userId;
	// 业务服务接收到的消息时间
	private long recTime;
	// 消息处理完的响应时间
	private long respTime;
	// 客户端的ip地址
	private String clientIp;
	private int fromServerId;
	private int toServerId;

	public int getFromServerId() {
		return fromServerId;
	}

	public void setFromServerId(int fromServerId) {
		this.fromServerId = fromServerId;
	}

	public int getToServerId() {
		return toServerId;
	}

	public void setToServerId(int toServerId) {
		this.toServerId = toServerId;
	}

	public String getClientIp() {

		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public long getRecTime() {
		return recTime;
	}

	public void setRecTime(long recTime) {
		this.recTime = recTime;
	}

	public long getRespTime() {
		return respTime;
	}

	public void setRespTime(long respTime) {
		this.respTime = respTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
}
