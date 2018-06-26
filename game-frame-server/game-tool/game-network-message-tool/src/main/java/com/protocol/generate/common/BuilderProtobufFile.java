package com.protocol.generate.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.protocol.generate.model.CommandObject;
import com.protocol.generate.model.ObjectConstans;
import com.protocol.generate.model.ProtobufMessage;
import com.protocol.generate.model.ProtocolObject;
import com.protocol.generate.model.StructObject;
import com.xinyue.utils.StringUtil;

public class BuilderProtobufFile {
	public static void build(ProtocolObject protocolObject) {
		List<ProtobufMessage> protobufMessages = new ArrayList<>();
		// 生成structs
		Collection<StructObject> structObjects = protocolObject.getStructObjectMap().values();
		for (StructObject structObject : structObjects) {
			ProtobufMessage protobufMessage = new ProtobufMessage();
			protobufMessage.setMessageName(structObject.getName() + ObjectConstans.StructModelName);
			protobufMessage.setFields(structObject.getFields());
			protobufMessages.add(protobufMessage);
		}
		Collection<CommandObject> commandObjects = protocolObject.getCommandObjectMap().values();
		for (CommandObject commandObject : commandObjects) {
			// 生成请求的结构
			ProtobufMessage protobufMessage = new ProtobufMessage();
			protobufMessage.setMessageName(commandObject.getCommandName() + ObjectConstans.RequestModelName);
			protobufMessage.setFields(commandObject.getRequestList());
			protobufMessages.add(protobufMessage);
			// 生成返回结构
			ProtobufMessage protobufMessage2 = new ProtobufMessage();
			protobufMessage2.setMessageName(commandObject.getCommandName() + ObjectConstans.ResponseModelName);
			protobufMessage2.setFields(commandObject.getResponseList());
			protobufMessages.add(protobufMessage2);
		}
		String outClassName = StringUtil.firstToUpper(protocolObject.getFileName());
		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("messages", protobufMessages);
		modelMap.put("className", outClassName);
		modelMap.put("packageName", protocolObject.getPackageName() + ObjectConstans.ProtoClassPath);
		String templateName = "JavaProtobufTemplate.ftl";
		String outPath = "config/java_proto";
		TemplateUtil.build(modelMap, templateName, outPath, outClassName + ".proto");
	}
}
