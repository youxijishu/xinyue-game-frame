package com.xinyue.network.message.common;

import com.xinyue.network.message.inner.InnerMessageHeader;

public abstract class AbstractGameMessage implements IGameMessage {
	private InnerMessageHeader messageHead;

	public AbstractGameMessage() {
		messageHead = new InnerMessageHeader();
		messageHead.setMessageId(this.getMessageId());
		messageHead.setGameMessageType(this.getGameMessageType());
	}

	@Override
	public InnerMessageHeader getMessageHead() {
		return messageHead;
	}

	protected void copyMessageHead(InnerMessageHeader responseMessageHead) {
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

	public void setError(IGameError gameError) {
		this.messageHead.setErrorCode(gameError.getErrorCode());
		this.messageHead.setErrorMsg(gameError.getErrorMsg());
	}
}
