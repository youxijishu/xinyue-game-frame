package com.protocol.generate.model;

import java.util.Map;

public class ProtocolObject {
	private String packageName;
	private String fileName;
	private Map<String, StructObject> structObjectMap;
	private Map<String, CommandObject> commandObjectMap;
	
	
	public Map<String, CommandObject> getCommandObjectMap() {
		return commandObjectMap;
	}
	public void setCommandObjectMap(Map<String, CommandObject> commandObjectMap) {
		this.commandObjectMap = commandObjectMap;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Map<String, StructObject> getStructObjectMap() {
		return structObjectMap;
	}
	public void setStructObjectMap(Map<String, StructObject> structObjectMap) {
		this.structObjectMap = structObjectMap;
	}
	
	
}
