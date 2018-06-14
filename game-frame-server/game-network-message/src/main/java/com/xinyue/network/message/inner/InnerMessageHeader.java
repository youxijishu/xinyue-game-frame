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
	// 网关接收到消息的时间
	private long grecTime;
	// 业务服务接收到的消息时间
	private long lrecTime;
	// 消息处理完的响应时间
	private long respTime;
	// 客户端的ip地址
	private String clientIp;
	//客户端的ip地址，使用int表示
	private int i_clientIp;
	
	
	public int getI_clientIp() {
		return i_clientIp;
	}

	public void setI_clientIp(int i_clientIp) {
		this.i_clientIp = i_clientIp;
	}

	public String getClientIp() {
		if(clientIp == null && i_clientIp != 0){
			
		}
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public long getGrecTime() {
		return grecTime;
	}

	public void setGrecTime(long grecTime) {
		this.grecTime = grecTime;
	}

	public long getLrecTime() {
		return lrecTime;
	}

	public void setLrecTime(long lrecTime) {
		this.lrecTime = lrecTime;
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
