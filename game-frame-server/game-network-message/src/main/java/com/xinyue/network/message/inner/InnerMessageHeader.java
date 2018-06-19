package com.xinyue.network.message.inner;

import com.xinyue.model.GameCommonConstants;
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

	/**
	 * 
	 * @Desc 获取网关向逻辑服务发送消息的时候，逻辑服务需要监听这个消息的tag，网关服务发送消息的时候，需要指定这个tag.这个只有在请求的消息中，所以toServerId是逻辑服务的id，fromServerId是网关的服务id
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月19日 下午10:36:17
	 *
	 */
	public String getToLogicServerMessageTag() {

		StringBuilder tagBuild = new StringBuilder();
		tagBuild.append(GameCommonConstants.MessageTagPrefix).append(this.getServerType().getServerType()).append(":")
				.append(this.getMessageId()).append(":").append(this.getToServerId());
		return tagBuild.toString();
	}

	/**
	 * 
	 * @Desc 当业务服务向网关服务发送消息的时候，需要给消息添加这个tag,网关需要监听这个tag.这个只有在消息返回的时候才会使用。所以toServer就是指网关的消息id，fromServerid是当前业务服务的id
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月19日 下午10:37:33
	 *
	 */
	public String getToGateServerMessageTag() {
		return String.valueOf(this.toServerId);
	}

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
