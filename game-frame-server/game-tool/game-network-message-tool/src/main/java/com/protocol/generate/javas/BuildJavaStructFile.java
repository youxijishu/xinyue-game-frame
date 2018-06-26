package com.protocol.generate.javas;

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

public class BuildJavaStructFile {

	public static void build(String javaPath, ProtocolObject protocolObject) {
		Collection<StructObject> structObjects = protocolObject.getStructObjectMap().values();
		javaPath += "/" + ObjectConstans.StructClassPath.substring(1);
		for (StructObject structObject : structObjects) {
			Map<String, Object> root = new HashMap<>();
			root.put("packageName", protocolObject.getPackageName() + ObjectConstans.StructClassPath);
			String className = StringUtil.firstToUpper(structObject.getName());
			root.put("className", className);
			root.put("fields", structObject.getFields());
			List<String> imports = new ArrayList<>();
			for (FieldObject fieldObject : structObject.getFields()) {
				if (fieldObject.getAttr().equals("repeated")) {
					imports.add("import java.util.List;");
					imports.add("import java.util.Collections;");
					imports.add("import java.util.ArrayList;");
					break;
				}
			}
			root.put("imports", imports);
			String builder = protocolObject.getPackageName() + ObjectConstans.ProtoClassPath + "."
					+ StringUtil.firstToUpper(protocolObject.getFileName());
			root.put("builder", builder);

			String fileName = className + ".java";
			TemplateUtil.build(root, "JavaStructTemplate.ftl", javaPath, fileName);
		}
	}
}
