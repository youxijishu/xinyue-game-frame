package com.xinyue.rocketmq.framework;

import com.xinyue.network.message.common.IGameMessage;

public interface MessageSendFactory {

	void sendMessage(IGameMessage gameMessage) throws Exception;
}
