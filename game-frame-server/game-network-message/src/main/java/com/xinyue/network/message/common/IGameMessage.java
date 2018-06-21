package com.xinyue.network.message.common;

import com.xinyue.network.message.inner.InnerMessageHeader;

public interface IGameMessage {
	int getMessageUniqueId();
	InnerMessageHeader getMessageHead();

	byte[] encodeBody() throws Exception;

	void decodeBody(byte[] bytes) throws Exception;

}
