package com.protocol.generate.csharps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.protocol.generate.common.TemplateUtil;
import com.protocol.generate.model.FieldObject;
import com.protocol.generate.model.ObjectConstans;
import com.protocol.generate.model.ProtocolObject;
import com.protocol.generate.model.StructObject;
import com.xinyue.utils.StringUtil;

public class BuildCSharpStructs {

	public static void build(String outPath,ProtocolObject protocolObject){
		Collection<StructObject> structObjects = protocolObject.getStructObjectMap().values();
		outPath += "/" + ObjectConstans.StructClassPath.substring(1);
		for(StructObject structObject : structObjects){
			Map<String, Object> root = new HashMap<>();
			root.put("namespace", protocolObject.getPackageName());
			String className =  StringUtil.firstToUpper(structObject.getName());
			root.put("className",className);
			root.put("fields", structObject.getFields());
			List<String> imports = new ArrayList<>();
			for(FieldObject fieldObject : structObject.getFields()){
				if(fieldObject.getAttr().equals("repeated")){
					imports.add("using System.Collections.Generic;");
					break;
				}
			}
			root.put("imports", imports);
			
			String fileName = className + ".cs";
			TemplateUtil.build(root, "CSharpStructTemplate.ftl", outPath,fileName);
		}
	}
}
