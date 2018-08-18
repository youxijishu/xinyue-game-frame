package com.xinyue.rocketmq.framework;

import com.xinyue.network.message.InnerMessageCodecFactory;
import com.xinyue.network.message.common.GameMessageHead;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.rocketmq.GameMessageTag;

import io.netty.buffer.ByteBuf;

public class LogicMessageSendFactory implements MessageSendFactory {

	private LogicMessageRouter messageRouter;

	public LogicMessageSendFactory(LogicMessageRouter messageRouter) {
		this.messageRouter = messageRouter;
	}

	@Override
	public void sendMessage(IGameMessage gameMessage) throws Exception {
		GameMessageHead messageHead = gameMessage.getMessageHead();

		long roleId = messageHead.getRoleId();
		int serverType = messageHead.getServerType();
		int messageId = messageHead.getMessageId();
		int serverId = messageHead.getToServerId();

		GameMessageTag messageTag = new GameMessageTag(serverType, messageId, serverId);
		String tag = messageTag.getTag();
		ByteBuf byteBuf = InnerMessageCodecFactory.getInstance().gameServerMessageEncode(gameMessage);
		messageRouter.sendMessage(roleId, tag, byteBuf);
	}

}
