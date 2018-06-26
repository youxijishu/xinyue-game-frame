package com.xinyue.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	public static byte[] InputStream2ByteArray(String filePath) throws IOException {

		InputStream in = new FileInputStream(filePath);
		byte[] data = toByteArray(in);
		in.close();

		return data;
	}

	private static byte[] toByteArray(InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}
}
