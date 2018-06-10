package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;

//河南心悦网络科技有限公司   王广帅
//连接认证

@GameMessageMetaData(id = 1001, type =GameMessageType.RESPONSE)
public class ConnectConfirmResponse extends AbstractGameMessage {

	private final static int MessageId = 1001;
	private final static GameMessageType type = GameMessageType.RESPONSE;
	
	public ConnectConfirmResponse(){
	}
	
	
	public ConnectConfirmRequest newResponse() {
		ConnectConfirmRequest response = new ConnectConfirmRequest();
		copyMessageHead(response.getMessageHead());
		return response;
	}

	@Override
	protected int getMessageId() {
		return MessageId;
	}

	@Override
	protected GameMessageType getGameMessageType() {
		return type;
	}

	
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 return null;
	}
	@Override
	public String toString(){
		String info = "ConnectConfirmResponse->" + JsonUtil.objToJson(this);
		return info;
	}
}
