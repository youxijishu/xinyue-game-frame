package com.xinyue.rocketmq;

/**
 * 向rocketmq发送消息时的tag组成信息类
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年8月14日 下午11:12:47
 */
public class GameMessageTag {
	private static final String Split = "-";
	//服务类型
	private int serverType;
	//消息的id
	private int messageId;
	//服务器的id
	private int serverId;
	
	
	public GameMessageTag(int serverType, int messageId, int serverId) {
		super();
		this.serverType = serverType;
		this.messageId = messageId;
		this.serverId = serverId;
	}


	public String getTag(){
		StringBuilder tag = new StringBuilder();
		tag.append(serverType).append(Split).append(messageId).append(Split).append(serverId);
		return tag.toString();
	}
	@Override
	public String toString(){
		return this.getTag();
	}
}
