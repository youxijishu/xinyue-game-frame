package com.protocol.generate.javas;

public class JavaFieldTypeUtil {

	public static String getType(String type, String attr) {
		String javaType = type;
		switch (type) {
		case "bool":
			javaType = "boolean";
			break;
		case "string":
			javaType = "String";
			break;
		case "int":
			if (attr.equals("repeated")) {
				javaType = "Integer";
			}
			break;
		case "long":
			if (attr.equals("repreated")) {
				javaType = "Long";
			}
		default:
			break;
		}
		return javaType;
	}

	public static boolean isJavaType(String type) {
		if (type.equals("int")) {
			return true;
		}
		if (type.equals("long")) {
			return true;
		}
		if (type.equals("string")) {
			return true;
		}
		if (type.equals("bool")) {
			return true;
		}
		if (type.equals("double")) {
			return true;
		}
		if (type.equals("float")) {
			return true;
		}
		return false;
	}
}
