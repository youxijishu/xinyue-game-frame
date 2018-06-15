package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;
import com.xinyue.network.EnumServerType;

//河南心悦网络科技有限公司   王广帅
//连接认证

@GameMessageMetaData(serverType = EnumServerType.GAME_SERVER,id = 1001, type =GameMessageType.RESPONSE)
public class ConnectConfirmResponse extends AbstractGameMessage {
	
	public ConnectConfirmResponse(){
	}
	
	
	public ConnectConfirmRequest newResponse() {
		ConnectConfirmRequest response = new ConnectConfirmRequest();
		copyMessageHead(response.getMessageHead());
		return response;
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
