package com.protocol.generate.csharps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.protocol.generate.common.TemplateUtil;
import com.protocol.generate.javas.JavaFieldTypeUtil;
import com.protocol.generate.model.CommandObject;
import com.protocol.generate.model.FieldObject;
import com.protocol.generate.model.ObjectConstans;
import com.protocol.generate.model.ProtocolObject;
import com.xinyue.utils.StringUtil;

public class BuildCsharpCommand {

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
			if (fieldObject.getArrayType() == 1) {
				String str = "System.Collections.Generic";
				if (!imports.contains(str)) {
					imports.add(str);
				}
			}
			if (!JavaFieldTypeUtil.isJavaType(fieldObject.getType())) {
				String importClass = packageName + ObjectConstans.StructClassPath;
				imports.add(importClass);
			} else {
				fieldObject.setBasicType(1);
			}
		}
		root.put("serverType", serverType);
		root.put("imports", imports);
		root.put("namespace", packageName);
		String className = StringUtil.firstToUpper(commandObject.getCommandName());
		if (request) {
			className += ObjectConstans.Request;
			root.put("messageType", "EnumMessageType.REQUEST");
		} else {
			className += ObjectConstans.Response;
			root.put("messageType", "EnumMessageType.RESPONSE");
		}
		root.put("className", className);

		root.put("fields", fieldObjects);

		root.put("id", commandObject.getCommandId());
		root.put("desc", commandObject.getDesc());
		String commandBody = StringUtil.firstToUpper(commandObject.getCommandName());
		if (request) {
			commandBody += ObjectConstans.RequestModelName;
		} else {
			commandBody += ObjectConstans.ResponseModelName;
		}
		root.put("commandBody", commandBody);
		String javaFileName = className + ".cs";
		TemplateUtil.build(root, "CSharpCommandTemplate.ftl", commandPath, javaFileName);
	}
}
