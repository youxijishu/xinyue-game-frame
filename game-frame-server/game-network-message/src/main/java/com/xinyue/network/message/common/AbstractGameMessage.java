package com.xinyue.network.message.common;

import com.xinyue.network.message.inner.InnerMessageHeader;

public abstract class AbstractGameMessage implements IGameMessage {
	private InnerMessageHeader messageHead;
	

	public AbstractGameMessage() {
		messageHead = new InnerMessageHeader();
		this.readGameMessageMeta();
		
	}

	private void readGameMessageMeta() {
		GameMessageMetaData gameMessageMetaData = this.getClass().getAnnotation(GameMessageMetaData.class);
		messageHead.setServerType(gameMessageMetaData.serverType());
		messageHead.setMessageId(gameMessageMetaData.messageId());
		messageHead.setGameMessageType(gameMessageMetaData.messageType());
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
			responseMessageHead.setFromServerId(this.messageHead.getToServerId());
			responseMessageHead.setToServerId(this.messageHead.getFromServerId());
			responseMessageHead.setRecTime(this.messageHead.getRecTime());
		} else {
			throw new UnsupportedOperationException("Response 消息不能创建返回消息");
		}
	}

	public void setError(IGameError gameError) {
		this.messageHead.setErrorCode(gameError.getErrorCode());
		this.messageHead.setErrorMsg(gameError.getErrorMsg());
	}
}
