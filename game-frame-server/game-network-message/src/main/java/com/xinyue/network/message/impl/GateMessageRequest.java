package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;
import com.xinyue.network.EnumServerType;

//河南心悦网络科技有限公司   王广帅
//连接认证

@GameMessageMetaData(serverType = EnumServerType.GATE,messageId = 1001, messageType =GameMessageType.REQUEST)
public class GateMessageRequest extends AbstractGameMessage {
	//消息内容
	private String message;
	
	public GateMessageRequest(){
	}
	public GateMessageRequest(String message){
		this.message = message;		
	}
	
	
	public GateMessageResponse newResponse() {
		GateMessageResponse response = new GateMessageResponse();
		copyMessageHead(response.getMessageHead());
		return response;
	}
	
	public void setMessage (String message){
		this.message = message;
	}
		
	public String getMessage(){
		return message;
	}
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		com.xinyue.network.message.impl.proto.Gate.GateMessageRequestModel body = com.xinyue.network.message.impl.proto.Gate.GateMessageRequestModel.parseFrom(bytes);
		
			this.message = body.getMessage();
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 com.xinyue.network.message.impl.proto.Gate.GateMessageRequestModel.Builder builder = com.xinyue.network.message.impl.proto.Gate.GateMessageRequestModel.newBuilder();
		  builder.setMessage(this.message);
		 return builder.build().toByteArray();
	}
	@Override
	public String toString(){
		String info = "GateMessageRequest->" + JsonUtil.objToJson(this);
		return info;
	}
}
