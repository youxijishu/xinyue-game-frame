package com.protocol.generate.javas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.protocol.generate.common.TemplateUtil;
import com.protocol.generate.model.CommandObject;
import com.protocol.generate.model.FieldObject;
import com.protocol.generate.model.ObjectConstans;
import com.protocol.generate.model.ProtocolObject;
import com.xinyue.string.StringUtil;

public class BuildJavaCommand {

	public static void build(ProtocolObject protocolObject, String commandPath) {

		Collection<CommandObject> commandObjects = protocolObject.getCommandObjectMap().values();
		for (CommandObject commandObject : commandObjects) {
			buildCommand(protocolObject.getServerType(),protocolObject.getFileName(), protocolObject.getPackageName(), commandObject,
					commandObject.getRequestList(), commandPath, true);

			buildCommand(protocolObject.getServerType(),protocolObject.getFileName(), protocolObject.getPackageName(), commandObject,
					commandObject.getResponseList(), commandPath, false);

		}
	}

	private static void buildCommand(String serverType,String fileName, String packageName, CommandObject commandObject,
			List<FieldObject> fieldObjects, String commandPath, boolean request) {
		Map<String, Object> root = new HashMap<>();
		List<String> imports = new ArrayList<>();
		for (FieldObject fieldObject : fieldObjects) {
			if (fieldObject.getAttr().equals("repeated")) {
				imports.add(" java.util.List");
				imports.add(" java.util.Collections");
				imports.add(" java.util.ArrayList");
				break;
			}
		}
		for (FieldObject fieldObject : fieldObjects) {
			if (!JavaFieldTypeUtil.isJavaType(fieldObject.getType())) {
				String importClass = packageName + ObjectConstans.StructClassPath + "."
						+ StringUtil.firstToUpper(fieldObject.getName());
				imports.add(importClass);
			} else {
				fieldObject.setBasicType(1);
			}
		}
		String builder = packageName + ObjectConstans.ProtoClassPath + "." + StringUtil.firstToUpper(fileName);
		root.put("builder", builder);
		root.put("imports", imports);
		root.put("packageName", packageName);
		root.put("serverType", serverType);
		String className = StringUtil.firstToUpper(commandObject.getCommandName());
		if (request) {
			root.put("newResponse", className + ObjectConstans.Response);
			className += ObjectConstans.Request;
			root.put("isRequest", 1);
			root.put("messageType", "GameMessageType.REQUEST");
		} else {

			root.put("newResponse", className + ObjectConstans.Request);
			className += ObjectConstans.Response;
			root.put("isRequest", 0);
			root.put("messageType", "GameMessageType.RESPONSE");
		}
		root.put("className", className);

		root.put("fields", fieldObjects);

		root.put("id", commandObject.getCommandId());
		root.put("desc", commandObject.getDesc());
		String commandBody = packageName + ObjectConstans.ProtoClassPath + "." + StringUtil.firstToUpper(fileName) + "."
				+ StringUtil.firstToUpper(commandObject.getCommandName());
		if (request) {
			commandBody += ObjectConstans.RequestModelName;
		} else {
			commandBody += ObjectConstans.ResponseModelName;
		}
		root.put("commandBody", commandBody);

		String javaFileName = className + ".java";
		TemplateUtil.build(root, "CommandTemplate.ftl", commandPath, javaFileName);
	}
}
