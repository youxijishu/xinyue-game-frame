package com.xinyue.network.message.common;

public interface IGameMessage {

	MessageHead getMessageHead();

	byte[] encodeBody() throws Exception;

	void decodeBody(byte[] bytes) throws Exception;

}
