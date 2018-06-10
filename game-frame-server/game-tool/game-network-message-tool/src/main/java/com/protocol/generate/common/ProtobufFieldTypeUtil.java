package com.protocol.generate.common;

import com.protocol.generate.model.ObjectConstans;

public class ProtobufFieldTypeUtil {

	public static String getType(String type){
		String protoType = type;
		switch (type) {
		case "int":
			protoType = "int32";
			break;
		case "long":
			protoType = "int64";
			break;
		case "string":
		case "double":
		case "float":
		case "bool":
			break;
		default:
			protoType += ObjectConstans.StructModelName;
			break;
		}
		return protoType;
	}
}
