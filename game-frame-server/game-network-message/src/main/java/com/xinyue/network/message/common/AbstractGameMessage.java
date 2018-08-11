package com.xinyue.network.message.common;

public abstract class AbstractGameMessage implements IGameMessage {
	private GameMessageHead messageHead;

	public AbstractGameMessage() {
		messageHead = new GameMessageHead();
		this.readGameMessageMeta();

	}

	private void readGameMessageMeta() {
		GameMessageMetaData gameMessageMetaData = this.getClass().getAnnotation(GameMessageMetaData.class);
		messageHead.setServerType(gameMessageMetaData.serverType().getServerType());
		messageHead.setMessageId(gameMessageMetaData.messageId());
		messageHead.setMessageType(gameMessageMetaData.messageType());
	}

	@Override
	public GameMessageHead getMessageHead() {
		return messageHead;
	}

	@Override
	public void setMessageHead(GameMessageHead messageHead) {
		messageHead.setServerType(this.messageHead.getServerType());
		messageHead.setMessageType(this.messageHead.getMessageType());
		this.messageHead = messageHead;
	}

	protected void copyMessageHead(GameMessageHead responseMessageHead) {
		if (this.messageHead.getMessageType() == GameMessageType.REQUEST) {
			responseMessageHead.setUserId(this.messageHead.getUserId());
			responseMessageHead.setRoleId(this.messageHead.getRoleId());
			responseMessageHead.setSeqId(this.messageHead.getSeqId());
			responseMessageHead.setFromeServerId(this.messageHead.getToServerId());
			responseMessageHead.setToServerId(this.messageHead.getFromeServerId());
			responseMessageHead.setIp(this.messageHead.getIp());
			responseMessageHead.setSendTime(System.currentTimeMillis());
			responseMessageHead.setSeqId(this.messageHead.getSeqId());
		} else {
			throw new UnsupportedOperationException("Response 消息不能创建返回消息");
		}
	}

	public void setError(IGameError gameError) {
		this.messageHead.setError(gameError);
	}
}
