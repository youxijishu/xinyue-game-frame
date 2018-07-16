package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;
import com.xinyue.network.EnumServerType;

//河南心悦网络科技有限公司   王广帅
//连接认证

@GameMessageMetaData(serverType = EnumServerType.GAME_SERVER,messageId = 1001, messageType =GameMessageType.RESPONSE)
public class ConnectConfirmResponse extends AbstractGameMessage {
	//是否成功
	private boolean result;
	
	public ConnectConfirmResponse(){
	}
	public ConnectConfirmResponse(boolean result){
		this.result = result;		
	}
	
	
	public ConnectConfirmRequest newResponse() {
		ConnectConfirmRequest response = new ConnectConfirmRequest();
		copyMessageHead(response.getMessageHead());
		return response;
	}
	
	public void setResult (boolean result){
		this.result = result;
	}
		
	public boolean getResult(){
		return result;
	}
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmResponseModel body = com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmResponseModel.parseFrom(bytes);
		
			this.result = body.getResult();
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmResponseModel.Builder builder = com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmResponseModel.newBuilder();
		  builder.setResult(this.result);
		 return builder.build().toByteArray();
	}
	@Override
	public String toString(){
		String info = "ConnectConfirmResponse->" + JsonUtil.objToJson(this);
		return info;
	}
}
