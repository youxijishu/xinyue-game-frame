package com.protocol.generate.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.protocol.generate.javas.JavaFieldTypeUtil;
import com.protocol.generate.model.CommandObject;
import com.protocol.generate.model.FieldObject;
import com.protocol.generate.model.ProtocolObject;
import com.protocol.generate.model.StructObject;
import com.xinyue.utils.StringUtil;
import com.xinyue.utils.XmlUtil;

public class ReadProtocolConfigFile {
	public ProtocolObject readFile(File file) throws DocumentException {
		Document doc = XmlUtil.getDocument(file);
		Element rootDoc = doc.getRootElement();
		ProtocolObject protocolObject = new ProtocolObject();
		String packageName = rootDoc.attributeValue("package");
		if (packageName == null || packageName.isEmpty()) {
			throw new NullPointerException("package no value");
		}
		protocolObject.setPackageName(packageName);
		String serverType = rootDoc.attributeValue("serverType");
		if(serverType == null || serverType.isEmpty()){
			throw new NullPointerException("serverType必须有值");
		}
		protocolObject.setServerType(serverType);
		readStructs(protocolObject, rootDoc);
		readCommands(protocolObject, rootDoc);
		return protocolObject;
	}

	private void readCommands(ProtocolObject protocolObject, Element root) {
		Element commandsEle = root.element("Commands");
		@SuppressWarnings("unchecked")
		Iterator<Element> commandIte = commandsEle.elementIterator("command");
		Map<String, CommandObject> commandMap = new HashMap<>();
		while (commandIte.hasNext()) {
			CommandObject commandObject = new CommandObject();
			Element commandEle = commandIte.next();
			String commandName = commandEle.attributeValue("name");
			String commandId = commandEle.attributeValue("id");
			String desc = commandEle.attributeValue("desc");
			commandObject.setCommandName(commandName);
			commandObject.setCommandId(StringUtil.valueOfInt(commandId));
			commandObject.setDesc(desc);
			Element requestEle = commandEle.element("request");
			List<FieldObject> requestFieldList = getCommandFieldObjectList(requestEle);
			commandObject.setRequestList(requestFieldList);
			Element responseEle = commandEle.element("response");
			List<FieldObject> responseFieldList = getCommandFieldObjectList(responseEle);
			commandObject.setResponseList(responseFieldList);
			commandMap.put(commandName, commandObject);
		}
		protocolObject.setCommandObjectMap(commandMap);
	}

	private List<FieldObject> getCommandFieldObjectList(Element ele) {
		@SuppressWarnings("unchecked")
		Iterator<Element> fieldIte = ele.elementIterator("field");
		List<FieldObject> requestFieldList = new ArrayList<>();
		int fieldIndex = 0;
		while (fieldIte.hasNext()) {
			fieldIndex ++;
			Element fieldEle = fieldIte.next();
			FieldObject fieldObject = readFieldObject(fieldEle);
			fieldObject.setIndex(fieldIndex);
			requestFieldList.add(fieldObject);
		}
		return requestFieldList;
	}

	private void readStructs(ProtocolObject protocolObject, Element root) {
		Element structsEle = root.element("Structs");
		if (structsEle != null) {
			@SuppressWarnings("unchecked")
			Iterator<Element> structIte = structsEle.elementIterator("struct");
			Map<String, StructObject> structMap = new HashMap<>();
			int structPos = 0;
			while (structIte.hasNext()) {
				structPos++;
				Element structEle = structIte.next();
				StructObject structObject = new StructObject();
				String structName = structEle.attributeValue("name");
				if (structName == null || structName.isEmpty()) {
					throw new NullPointerException("第" + structPos + "个struct没有配置name");
				}
				structObject.setName(structName);
				@SuppressWarnings("unchecked")
				Iterator<Element> fieldIte = structEle.elementIterator("field");
				int fieldPos = 0;
				List<FieldObject> fieldList = new ArrayList<>();
				while (fieldIte.hasNext()) {
					fieldPos++;
					Element fieldEle = fieldIte.next();
					FieldObject fieldObject = readFieldObject(fieldEle);
					String fieldName = fieldObject.getName();
					if (fieldName == null || fieldName.isEmpty()) {
						throw new NullPointerException("第" + structPos + "struct的第" + fieldPos + "的field没有配置name");
					}
					String fieldType = fieldObject.getType();
					if (fieldType == null || fieldType.isEmpty()) {
						throw new NullPointerException("第" + structPos + "struct的第" + fieldPos + "的field没有配置name");
					}
					fieldObject.setIndex(fieldPos);
					
					fieldList.add(fieldObject);
				}
				structObject.setFields(fieldList);
				structMap.put(structName, structObject);
			}
			protocolObject.setStructObjectMap(structMap);
		}
	}

	private FieldObject readFieldObject(Element fieldEle) {
		FieldObject fieldObject = new FieldObject();
		String fieldName = fieldEle.attributeValue("name");

		fieldObject.setName(fieldName);

		String fieldType = fieldEle.attributeValue("type");
		if(JavaFieldTypeUtil.isJavaType(fieldType)){
			fieldObject.setBasicType(1);
		}
		fieldObject.setType(fieldType);

		String desc = fieldEle.attributeValue("desc");
		if (desc == null) {
			desc = "";
		}
		fieldObject.setDesc(desc);

		String attr = fieldEle.attributeValue("attr");
		if (attr == null || attr.isEmpty()) {
			attr = "required";
		}
		fieldObject.setAttr(attr);
		return fieldObject;
	}
}
