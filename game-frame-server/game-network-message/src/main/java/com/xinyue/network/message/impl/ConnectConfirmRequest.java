package com.xinyue.network.message.impl;
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;

//河南心悦网络科技有限公司   王广帅
//连接认证

@GameMessageMetaData(id = 1001, type =GameMessageType.REQUEST)
public class ConnectConfirmRequest extends AbstractGameMessage {

	private final static int MessageId = 1001;
	private final static GameMessageType type = GameMessageType.REQUEST;
	//token
	private String token;
	//userId
	private long userId;
	//角色id
	private long roleId;
	
	public ConnectConfirmRequest(){
	}
	public ConnectConfirmRequest(String token, long userId, long roleId){
		this.token = token;		
		this.userId = userId;		
		this.roleId = roleId;		
	}
	
	
	public ConnectConfirmResponse newResponse() {
		ConnectConfirmResponse response = new ConnectConfirmResponse();
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

	
	
	public void setToken (String token){
		this.token = token;
	}
		
	public String getToken(){
		return token;
	}
	
	public void setUserId (long userId){
		this.userId = userId;
	}
		
	public long getUserId(){
		return userId;
	}
	
	public void setRoleId (long roleId){
		this.roleId = roleId;
	}
		
	public long getRoleId(){
		return roleId;
	}
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmRequestModel body = com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmRequestModel.parseFrom(bytes);
		
			this.token = body.getToken();
		
			this.userId = body.getUserId();
		
			this.roleId = body.getRoleId();
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmRequestModel.Builder builder = com.xinyue.network.message.impl.proto.UserModule.ConnectConfirmRequestModel.newBuilder();
		  builder.setToken(this.token);
		  builder.setUserId(this.userId);
		  builder.setRoleId(this.roleId);
		 return builder.build().toByteArray();
	}
	@Override
	public String toString(){
		String info = "ConnectConfirmRequest->" + JsonUtil.objToJson(this);
		return info;
	}
}
