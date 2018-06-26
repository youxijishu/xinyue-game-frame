package com.protocol.generate.javas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.protocol.generate.common.TemplateUtil;
import com.protocol.generate.model.CommandObject;
import com.protocol.generate.model.ObjectConstans;
import com.protocol.generate.model.ProtocolObject;
import com.xinyue.utils.StringUtil;

public class BuildCommandFactory {

	public static void build(String javaPath, List<ProtocolObject> protocolObjects) {
		Map<String, Object> root = new HashMap<>();
		List<String> commandList = new ArrayList<>();
		for (ProtocolObject protocolObject : protocolObjects) {
			Collection<CommandObject> commandObjects = protocolObject.getCommandObjectMap().values();
			root.put("packageName", protocolObject.getPackageName());
			for (CommandObject commandObject : commandObjects) {
				String commandName = StringUtil.firstToUpper(commandObject.getCommandName()) + ObjectConstans.Request;
				commandList.add(commandName);
			}
		}
		root.put("commands", commandList);
		
		String fileName = "CommandFactory.java";
		TemplateUtil.build(root, "CommandFactory.ftl", javaPath, fileName);
	}
}
