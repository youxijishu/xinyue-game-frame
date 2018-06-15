package com.xinyue.network.message.common;

import com.xinyue.network.message.inner.InnerMessageHeader;

public abstract class AbstractGameMessage implements IGameMessage {
	private InnerMessageHeader messageHead;

	public AbstractGameMessage() {
		messageHead = new InnerMessageHeader();
		GameMessageMetaData gameMessageMetaData = this.getClass().getAnnotation(GameMessageMetaData.class);
		messageHead.setServerType(gameMessageMetaData.serverType());
		messageHead.setMessageId(gameMessageMetaData.id());
		messageHead.setGameMessageType(gameMessageMetaData.type());
	}

	@Override
	public InnerMessageHeader getMessageHead() {
		return messageHead;
	}

	protected void copyMessageHead(InnerMessageHeader responseMessageHead) {
		if (this.messageHead.getGameMessageType() == GameMessageType.REQUEST) {
			responseMessageHead.setUserId(this.messageHead.getUserId());
			responseMessageHead.setRoleId(this.messageHead.getRoleId());
			responseMessageHead.setSeqId(this.messageHead.getSeqId());
		} else {
			throw new UnsupportedOperationException("Response 消息不能创建返回消息");
		}
	}

	public void setError(IGameError gameError) {
		this.messageHead.setErrorCode(gameError.getErrorCode());
		this.messageHead.setErrorMsg(gameError.getErrorMsg());
	}
}
