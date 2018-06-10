package com.protocol.generate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;

import com.protocol.generate.common.BuilderProtobufFile;
import com.protocol.generate.common.ReadProtocolConfigFile;
import com.protocol.generate.javas.BuildJavaCommand;
import com.protocol.generate.javas.BuildJavaProtobufFile;
import com.protocol.generate.javas.BuildJavaStructFile;
import com.protocol.generate.model.ProtocolObject;

public class GenerateJavaCode {

	public static void main(String[] args) {
		// int i = 0;
		// String javaPath = args[i++];
		String javaPath = "E:\\java_dev\\game-frame-dev\\xinyue-game-frame\\game-frame-server\\game-network-message\\src\\main\\java";
		File fileDir = new File("config/proto_config");
		File[] configfile = fileDir.listFiles();
		ReadProtocolConfigFile readProtocolConfigFile = new ReadProtocolConfigFile();
		BuildJavaProtobufFile buildJavaProtobufFile = new BuildJavaProtobufFile();
		List<ProtocolObject> protocolObjects = new ArrayList<>();
		String commandDirPath = null;
		String desPath = "../../tool/Protocol/config/proto_config/";

		for (File file : configfile) {
			if (!file.getName().endsWith(".xml")) {
				continue;
			}

			try {
				try {
					FileUtils.copyFile(file, new File(desPath + file.getName()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				ProtocolObject protocolObject = readProtocolConfigFile.readFile(file);
				protocolObjects.add(protocolObject);
				// 清空旧的数据
				commandDirPath = javaPath + "/" + protocolObject.getPackageName().replace(".", "/");
				// File dir = new File(commandDirPath);
				// deleteDir(dir);
				String fileName = file.getName().substring(0, file.getName().length() - 4);
				protocolObject.setFileName(fileName);
				BuilderProtobufFile.build(protocolObject);
				boolean result = buildJavaProtobufFile.buildJava(fileName, javaPath);
				if (!result) {
					return;
				}
				// 生成java structs代码
				BuildJavaStructFile.build(commandDirPath, protocolObject);
				// 生成java command代码
				BuildJavaCommand.build(protocolObject, commandDirPath);

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}
