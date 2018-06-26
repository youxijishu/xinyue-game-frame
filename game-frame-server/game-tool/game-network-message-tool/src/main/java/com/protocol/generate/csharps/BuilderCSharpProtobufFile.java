package com.protocol.generate.csharps;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.xinyue.utils.ByteUtil;

public class BuilderCSharpProtobufFile {

	public static void build(String packageName, String outFilePath, String fileName) {
		String protoFile = "config/java_proto/" + fileName + ".proto";
		String outFile = outFilePath + "/proto";
		File dir = new File(outFile);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		outFile += "/" + fileName + ".cs";
		buildCsharp(protoFile, outFile, packageName + ".proto");
	}

	private static boolean buildCsharp(String protoFile, String outFile, String namespace) {
		try {
			String cmd = "config\\\\ProtoGen\\\\protogen.exe -i:" + protoFile + " -o:" + outFile + " -ns:" + namespace;
			System.out.println(cmd);
			Process process = Runtime.getRuntime().exec(cmd);
			InputStream in = process.getErrorStream();
			byte[] bytes = ByteUtil.readInputStream(in);
			if (bytes.length > 0) {
				System.out.println(new String(bytes));
			} else {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
