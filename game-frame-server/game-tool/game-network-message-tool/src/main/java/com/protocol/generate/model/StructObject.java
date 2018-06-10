package com.protocol.generate.model;

import java.util.List;

public class StructObject {
	private String name;
	private List<FieldObject> fields;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<FieldObject> getFields() {
		return fields;
	}
	public void setFields(List<FieldObject> fields) {
		this.fields = fields;
	}
	
	
}
