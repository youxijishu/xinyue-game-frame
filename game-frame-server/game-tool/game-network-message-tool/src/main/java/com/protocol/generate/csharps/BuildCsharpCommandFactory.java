package com.protocol.generate.csharps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.protocol.generate.common.TemplateUtil;
import com.protocol.generate.model.CommandObject;
import com.protocol.generate.model.CsharpFactoryMessage;
import com.protocol.generate.model.ObjectConstans;
import com.protocol.generate.model.ProtocolObject;
import com.xinyue.utils.StringUtil;

public class BuildCsharpCommandFactory {

	
	public static void build(List<ProtocolObject> protocolObjects,String outPath){
		Map<String, Object> root = new HashMap<>();
		List<CsharpFactoryMessage> commandList = new ArrayList<>();
		for (ProtocolObject protocolObject : protocolObjects) {
			Collection<CommandObject> commandObjects = protocolObject.getCommandObjectMap().values();
			root.put("namespace", protocolObject.getPackageName());
			for (CommandObject commandObject : commandObjects) {
				String commandName = StringUtil.firstToUpper(commandObject.getCommandName()) + ObjectConstans.Response;
				CsharpFactoryMessage message = new CsharpFactoryMessage(commandObject.getCommandId(), commandName);
				commandList.add(message);
			}
		}
		root.put("commands", commandList);
		
		String fileName = "CommandFactory.cs";
		TemplateUtil.build(root, "CsharpCommandFactory.ftl", outPath, fileName);
	}
}
