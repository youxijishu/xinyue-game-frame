package com.xinyue.game.channel;

import com.xinyue.network.message.common.IGameMessage;

public interface GameChannelOutboundInvoker {

	void writeMessage(IGameMessage message);
}
