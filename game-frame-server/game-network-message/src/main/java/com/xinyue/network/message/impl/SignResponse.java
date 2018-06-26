package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;
import com.xinyue.network.EnumServerType;

//河南心悦网络科技有限公司   王广帅
//签到

@GameMessageMetaData(serverType = EnumServerType.GAME_SERVER,messageId = 1002, messageType =GameMessageType.RESPONSE)
public class SignResponse extends AbstractGameMessage {
	//true 签到成功
	private boolean Result;
	
	public SignResponse(){
	}
	public SignResponse(boolean Result){
		this.Result = Result;		
	}
	
	
	public SignRequest newResponse() {
		SignRequest response = new SignRequest();
		copyMessageHead(response.getMessageHead());
		return response;
	}
	
	public void setResult (boolean Result){
		this.Result = Result;
	}
		
	public boolean getResult(){
		return Result;
	}
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		com.xinyue.network.message.impl.proto.UserModule.SignResponseModel body = com.xinyue.network.message.impl.proto.UserModule.SignResponseModel.parseFrom(bytes);
		
			this.Result = body.getResult();
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 com.xinyue.network.message.impl.proto.UserModule.SignResponseModel.Builder builder = com.xinyue.network.message.impl.proto.UserModule.SignResponseModel.newBuilder();
		  builder.setResult(this.Result);
		 return builder.build().toByteArray();
	}
	@Override
	public String toString(){
		String info = "SignResponse->" + JsonUtil.objToJson(this);
		return info;
	}
}
