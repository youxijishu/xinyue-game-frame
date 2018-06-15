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
			Integer messageId = gameMessageMetaData.id();
			GameMessageType messageType = gameMessageMetaData.type();
			if (messageType == GameMessageType.REQUEST) {
				messageClassMap.put(messageId, gameMessage);
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
	public IGameMessage getGameMessage(int messageId) throws InstantiationException, IllegalAccessException {
		Class<? extends IGameMessage> clazz = this.messageClassMap.get(messageId);
		if (clazz == null) {
			return null;
		}
		return clazz.newInstance();
	}

}
