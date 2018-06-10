package com.xinyue.network.message.common;

public abstract class AbstractGameMessage implements IGameMessage {
	private MessageHead messageHead;

	public AbstractGameMessage() {
		messageHead = new MessageHead();
		messageHead.setMessageId(this.getMessageId());
		messageHead.setGameMessageType(this.getGameMessageType());
	}

	@Override
	public MessageHead getMessageHead() {
		return messageHead;
	}

	protected void copyMessageHead(MessageHead responseMessageHead) {
		if (this.getGameMessageType() == GameMessageType.REQUEST) {
			responseMessageHead.setUserId(this.messageHead.getUserId());
			responseMessageHead.setRoleId(this.messageHead.getRoleId());
			responseMessageHead.setSeqId(this.messageHead.getSeqId());
		} else {
			throw new UnsupportedOperationException("Response 消息不能创建返回消息");
		}
	}

	protected abstract int getMessageId();

	protected abstract GameMessageType getGameMessageType();
}
