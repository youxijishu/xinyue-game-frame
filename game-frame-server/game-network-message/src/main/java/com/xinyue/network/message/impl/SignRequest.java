package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;
import com.xinyue.network.EnumServerType;

//河南心悦网络科技有限公司   王广帅
//签到

@GameMessageMetaData(serverType = EnumServerType.GAME_SERVER,id = 1002, type =GameMessageType.REQUEST)
public class SignRequest extends AbstractGameMessage {
	//角色id
	private long RoleId;
	
	public SignRequest(){
	}
	public SignRequest(long RoleId){
		this.RoleId = RoleId;		
	}
	
	
	public SignResponse newResponse() {
		SignResponse response = new SignResponse();
		copyMessageHead(response.getMessageHead());
		return response;
	}
	
	public void setRoleId (long RoleId){
		this.RoleId = RoleId;
	}
		
	public long getRoleId(){
		return RoleId;
	}
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		com.xinyue.network.message.impl.proto.UserModule.SignRequestModel body = com.xinyue.network.message.impl.proto.UserModule.SignRequestModel.parseFrom(bytes);
		
			this.RoleId = body.getRoleId();
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 com.xinyue.network.message.impl.proto.UserModule.SignRequestModel.Builder builder = com.xinyue.network.message.impl.proto.UserModule.SignRequestModel.newBuilder();
		  builder.setRoleId(this.RoleId);
		 return builder.build().toByteArray();
	}
	@Override
	public String toString(){
		String info = "SignRequest->" + JsonUtil.objToJson(this);
		return info;
	}
}
