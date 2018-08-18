package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;
import com.xinyue.network.EnumServerType;

//河南心悦网络科技有限公司   王广帅
//连接认证

@GameMessageMetaData(serverType = EnumServerType.GATE,messageId = 1001, messageType =GameMessageType.RESPONSE)
public class GateMessageResponse extends AbstractGameMessage {
	//返回消息
	private String result;
	
	public GateMessageResponse(){
	}
	public GateMessageResponse(String result){
		this.result = result;		
	}
	
	
	public GateMessageRequest newResponse() {
		GateMessageRequest response = new GateMessageRequest();
		copyMessageHead(response.getMessageHead());
		return response;
	}
	
	public void setResult (String result){
		this.result = result;
	}
		
	public String getResult(){
		return result;
	}
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		com.xinyue.network.message.impl.proto.Gate.GateMessageResponseModel body = com.xinyue.network.message.impl.proto.Gate.GateMessageResponseModel.parseFrom(bytes);
		
			this.result = body.getResult();
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 com.xinyue.network.message.impl.proto.Gate.GateMessageResponseModel.Builder builder = com.xinyue.network.message.impl.proto.Gate.GateMessageResponseModel.newBuilder();
		  builder.setResult(this.result);
		 return builder.build().toByteArray();
	}
	@Override
	public String toString(){
		String info = "GateMessageResponse->" + JsonUtil.objToJson(this);
		return info;
	}
}
