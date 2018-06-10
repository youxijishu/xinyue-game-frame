package com.protocol.generate.model;

import java.util.List;

public class StructsMessage {

	private String packageName;
	private String className;
	private List<String> imports;
	private List<FieldObject> fields;
	private String builder;
	
	
	public String getBuilder() {
		return builder;
	}
	public void setBuilder(String builder) {
		this.builder = builder;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<String> getImports() {
		return imports;
	}
	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	public List<FieldObject> getFields() {
		return fields;
	}
	public void setFields(List<FieldObject> fields) {
		this.fields = fields;
	}
	
	
}
