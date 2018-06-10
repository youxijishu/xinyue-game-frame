package com.protocol.generate.model;

import com.protocol.generate.common.ProtobufFieldTypeUtil;
import com.protocol.generate.csharps.CSharpTypeUtil;
import com.protocol.generate.javas.JavaFieldTypeUtil;

public class FieldObject {
	private String name;
	private String desc;
	private String type;
	private String protoType;
	private String javaType;
	private String csharpType;
	private String attr;
	private int index;
	// 基本类型：1，非基本类型：0
	private int basicType;
	// 如果是list：1，非list:0;
	private int arrayType;
	

	public int getArrayType() {
		if(this.attr .equals("repeated")){
			this.arrayType = 1;
		}
		return arrayType;
	}

	public void setArrayType(int arrayType) {
		this.arrayType = arrayType;
	}

	public String getCsharpType() {
		
		return csharpType;
	}

	public void setCsharpType(String csharpType) {
		this.csharpType = csharpType;
	}

	public int getBasicType() {
		return basicType;
	}

	public void setBasicType(int basicType) {
		this.basicType = basicType;
	}

	public String getProtoType() {
		return protoType;
	}

	public void setProtoType(String protoType) {
		this.protoType = protoType;
	}

	public String getJavaType() {
		
		return JavaFieldTypeUtil.getType(type,attr);
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.protoType = ProtobufFieldTypeUtil.getType(type);
		this.csharpType = CSharpTypeUtil.getType(type);
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

}
