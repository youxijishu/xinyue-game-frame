package com.xinyue.network.message.common;

public class MessageIdUtil {
	/**
	 * 
	 * @Desc  获取一个IGameMessage的唯一id，这个id由服务类型和messageId组成，因为不同服务类型的messageId可能是重复的。
	 * @param type
	 * @param messageId
	 * @return
	 * @Author 心悦网络  王广帅
	 * @Date 2018年6月21日 下午3:05:38
	 *
	 */
	public static int getMessageUniqueId(int serverType,int messageId){
		int messageId1 = messageId;
		return (serverType << 16) + messageId1;
	}


	public static short getMessageId(int uniqueMessageId) {
		short messageId = (short) (uniqueMessageId & 0xffff);
		return messageId;
	}

	public static short getServerType(int uniqueMessageId) {
		return (short) (uniqueMessageId >> 16);
	}

}
