package com.protocol.generate.javas;

import java.io.IOException;
import java.io.InputStream;

import com.xinyue.arithmetic.ByteUtil;
import com.xinyue.string.StringUtil;

public class BuildJavaProtobufFile {

	public boolean buildJava(String protoFile, String javaPath) {
		protoFile = "config/java_proto/" + StringUtil.firstToUpper(protoFile) + ".proto";

		try {
			String cmd = "config\\\\protoc.exe --java_out=" + javaPath + " " + protoFile;
			System.out.println(cmd);
			Process process = Runtime.getRuntime().exec(cmd);
			InputStream in = process.getErrorStream();
			byte[] b = ByteUtil.readInputStream(in);
			if (b.length == 0) {
				return true;
			}
			System.out.println(new String(b));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
