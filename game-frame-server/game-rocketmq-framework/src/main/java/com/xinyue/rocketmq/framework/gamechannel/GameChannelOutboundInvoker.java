package com.xinyue.rocketmq.framework.gamechannel;

import com.xinyue.network.message.common.IGameMessage;

public interface GameChannelOutboundInvoker {

	void writeMessage(IGameMessage message);

	void close();
}
