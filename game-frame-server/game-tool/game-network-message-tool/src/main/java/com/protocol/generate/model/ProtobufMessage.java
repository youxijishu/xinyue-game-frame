package com.protocol.generate.model;

import java.util.List;

public class ProtobufMessage {

	private String messageName;
	private List<FieldObject> fields;

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public List<FieldObject> getFields() {
		return fields;
	}

	public void setFields(List<FieldObject> fields) {
		this.fields = fields;
	}
	
	
	
}
