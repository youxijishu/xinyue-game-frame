package com.xinyue.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @Desc 描述:
 * @author wang guang shuai
 * @Date 2016年9月20日 上午10:51:40
 */
public class ByteUtil {
	static final char INTEGER_VALUE_ARRAY[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f', };

	public static final int DEFAULT_BYTE_COUNT_PERLINE = 8;

	public static String toHexString(byte[] buf) {
		return byteToHexStringShow(buf, 0, buf.length, DEFAULT_BYTE_COUNT_PERLINE);
	}

	/**
	 * @brief 把byte数值转换成 16进制的字符串显示
	 * @param buf
	 *            byte 数组
	 * @param offset
	 *            偏移量
	 * @param len
	 *            长度
	 * @param bytePerline
	 *            多少个byte打一行字符串
	 * @return 字符串
	 */
	public static String byteToHexStringShow(byte[] buf, int offset, int len, int bytePerline) {
		if (len == 0) {
			return "";
		}
		char buffer[] = new char[4];
		buffer[0] = '0';
		buffer[1] = 'x';
		int radix = 16;
		int perlineMax = bytePerline - 1;
		assert bytePerline > 0;
		assert len >= 0;
		assert offset >= 0 && offset + len <= buf.length;
		StringBuilder builder = new StringBuilder();
		// builder.reverse()
		for (int i = offset; i < offset + len; ++i) {
			int value = buf[i] & 0xff;
			buffer[3] = INTEGER_VALUE_ARRAY[value % radix];
			value /= radix;
			buffer[2] = INTEGER_VALUE_ARRAY[value % radix];
			builder.append(buffer);
			if ((i % bytePerline) == perlineMax) {
				builder.append('\n');
			} else {
				builder.append(' ');
			}
		}
		return builder.toString();
	}

	/**
	 * Convert byte[] to hex string. 把字节数组转化为字符串
	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[] 把为字符串转化为字节数组
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	public static byte[] readInputStream(InputStream in) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
		byte[] bytes = new byte[1024];
		int a = 0;
		try {
			while ((a = in.read(bytes)) != -1) {
				byte[] readByte = new byte[a];
				System.arraycopy(bytes, 0, readByte, 0, a);
				byteBuffer.put(readByte);
			}
			byteBuffer.flip();
			bytes = new byte[byteBuffer.remaining()];
			byteBuffer.get(bytes);
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
