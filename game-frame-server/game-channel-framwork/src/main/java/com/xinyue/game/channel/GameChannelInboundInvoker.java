package com.xinyue.game.channel;

import com.xinyue.network.message.common.IGameMessage;

public interface GameChannelInboundInvoker {

	GameChannelInboundInvoker fireReadMessage(IGameMessage message);

	GameChannelInboundInvoker fireExceptionCaught(Throwable cause);

	GameChannelInboundInvoker fireUserEventTriggered(Object event);
}
