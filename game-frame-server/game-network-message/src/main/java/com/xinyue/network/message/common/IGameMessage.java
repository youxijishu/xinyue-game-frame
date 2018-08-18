package com.xinyue.network.message.common;

public interface IGameMessage {
	
	GameMessageHead getMessageHead();
	
	void setMessageHead(GameMessageHead messageHead);

	byte[] encodeBody() throws Exception;

	void decodeBody(byte[] bytes) throws Exception;

}
