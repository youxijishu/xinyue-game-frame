package com.xinyue.network.message.impl;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;

@GameMessageMetaData(id = 1001, type = GameMessageType.RESPONSE)
public class ConnectConfirmResponse extends AbstractGameMessage {
	private final static int MessageId = 1001;
	private final static GameMessageType type = GameMessageType.RESPONSE;

	

	@Override
	protected int getMessageId() {
		return MessageId;
	}

	@Override
	protected GameMessageType getGameMessageType() {
		return type;
	}

	@Override
	public byte[] encodeBody() throws Exception {
		return null;
	}

	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		
	}

}
