package com.protocol.generate.model;

import java.util.List;

public class CommandObject {

	private String commandName;
	private int commandId;
	private String desc;
	private List<FieldObject> requestList;
	private List<FieldObject> responseList;
	
	
	
	public String getCommandName() {
		return commandName;
	}
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	public int getCommandId() {
		return commandId;
	}
	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<FieldObject> getRequestList() {
		return requestList;
	}
	public void setRequestList(List<FieldObject> requestList) {
		this.requestList = requestList;
	}
	public List<FieldObject> getResponseList() {
		return responseList;
	}
	public void setResponseList(List<FieldObject> responseList) {
		this.responseList = responseList;
	}
	
	
}
