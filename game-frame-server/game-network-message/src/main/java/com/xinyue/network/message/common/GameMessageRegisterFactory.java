package com.xinyue.network.message.common;

import java.util.HashMap;
import java.util.List;
/**
 * 负责消息的解码和编码及验证操作
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年6月10日 下午5:25:04
 */
import java.util.Map;

public class GameMessageRegisterFactory {
	private Map<Integer, Class<? extends IGameMessage>> messageClassMap = null;
	private static GameMessageRegisterFactory instance = new GameMessageRegisterFactory();

	public static GameMessageRegisterFactory getInstance() {
		return instance;
	}

	public int getUniqueMessageId(short serverType, short messageId) {
		int id = (((int) serverType) << 16) + ((int) messageId);
		return id;
	}

	public short getMessageId(int uniqueMessageId) {
		short messageId = (short) (uniqueMessageId & 0xffff);
		return messageId;
	}

	public short getServerType(int uniqueMessageId) {
		return (short) (uniqueMessageId >> 16);
	}

	/**
	 * 
	 * @Desc 设置要解码的消息的id和对应的class
	 * @param commandClassMap
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月10日 下午5:31:03
	 *
	 */
	public void registerCommandClass(List<Class<? extends IGameMessage>> gameMessages) {
		Map<Integer, Class<? extends IGameMessage>> messageClassMap = new HashMap<>();
		for (Class<? extends IGameMessage> gameMessage : gameMessages) {
			if (gameMessage == null) {
				continue;
			}
			GameMessageMetaData gameMessageMetaData = gameMessage.getAnnotation(GameMessageMetaData.class);
			if (gameMessageMetaData == null) {
				throw new IllegalArgumentException(gameMessage.getName() + "消息没有GameMessageMetaData");
			}
			short messageId = gameMessageMetaData.id();
			GameMessageType messageType = gameMessageMetaData.type();
			if (messageType == GameMessageType.REQUEST) {
				int uniqueMessageId = this.getUniqueMessageId(gameMessageMetaData.serverType().getServerType(),
						messageId);
				if (messageClassMap.containsKey(uniqueMessageId)) {
					throw new IllegalArgumentException("命令重复注册：serverType:"
							+ gameMessageMetaData.serverType().getServerType() + ",messageId:" + messageId);
				}
				messageClassMap.put(uniqueMessageId, gameMessage);
			}
		}
		this.messageClassMap = messageClassMap;
	}

	public boolean containsGameMessage(int messageId) {
		return this.messageClassMap.containsKey(messageId);
	}

	/**
	 * 
	 * @Desc 获取一个messageid对应的class创建的对象
	 * @param messageId
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月15日 下午4:34:19
	 *
	 */
	public IGameMessage getGameMessage(short serverType, short messageId)
			throws InstantiationException, IllegalAccessException {
		int uniqueMessage = this.getUniqueMessageId(serverType, messageId);
		Class<? extends IGameMessage> clazz = this.messageClassMap.get(uniqueMessage);
		if (clazz == null) {
			return null;
		}
		return clazz.newInstance();
	}

}
