package com.xinyue.utils;

public class StringUtil {

	/** 冒号 **/
	public static final String COLON = ":";
	/** 逗号 **/
	public static final String COMMA = ",";
	private final static String TRUE = "true";
	public final static String UNDERLINE = "_";
	/** 横线 **/
	public final static String LINE = "-";

	/**
	 * 
	 * @Desc 描述：将字节数组转化为字符串
	 * @param bytes
	 * @return
	 * @author wang guang shuai
	 * @date 2016年9月18日 下午3:48:13
	 *
	 */
	public static String byteToStr(byte[] bytes) {
		if (bytes != null) {
			String result = new String(bytes);
			return result;
		}
		return null;
	}

	/**
	 * 
	 * @Desc 描述：把字符串的第一个字母改为大小
	 * @param str
	 * @return
	 * @author wang guang shuai
	 * @date 2016年9月16日 下午3:58:58
	 *
	 */
	public static String firstToUpper(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

	/**
	 * 
	 * @Desc 描述：把字符串的第一个字母变成小写
	 * @param str
	 * @return
	 * @author wang guang shuai
	 * @date 2016年9月16日 下午3:59:13
	 *
	 */
	public static String firstToLower(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
	}

	/**
	 * 
	 * @Desc 描述：检测一个字符串是否为null或empty
	 * @param str
	 *            要检测的字符串
	 * @return 如果为null或empty返回true,否则返回false
	 * @author wang guang shuai
	 * @date 2016年9月16日 下午4:32:55
	 *
	 */
	public static boolean isNullEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 描述：字符串转化为byte
	 *
	 * @param str
	 * @return
	 * @author wang guang shuai
	 *
	 *         2016年10月26日 下午2:59:35
	 *
	 */
	public static byte valueOfByte(String str) {
		byte value = 0;
		if (isNullEmpty(str)) {
			return value;
		}
		value = Byte.parseByte(str);
		return value;
	}

	/**
	 * 
	 * @Desc 描述：将字符串转化为整数
	 * @param str
	 * @return 如果字符串为null或empty,返回0
	 * @author wang guang shuai
	 * @date 2016年9月16日 下午4:34:05
	 *
	 */
	public static int valueOfInt(String str) {
		int value = 0;
		if (isNullEmpty(str)) {
			return value;
		}
		value = Integer.parseInt(str);
		return value;
	}

	public static long valueOfLong(String str) {
		long value = 0;
		if (isNullEmpty(str)) {
			return value;
		}
		value = Long.parseLong(str);
		return value;
	}

	public static float valueOfFloat(String str) {
		float value = 0;
		if (isNullEmpty(str)) {
			return value;
		}
		value = Float.parseFloat(str);
		return value;
	}

	public static boolean valueOfBoolean(String str) {
		boolean value = false;
		if (!isNullEmpty(str)) {
			str = str.toLowerCase();
			if (str.equals(TRUE)) {
				value = true;
			}
		}
		return value;
	}

	/**
	 * 
	 * 描述：将二进制的字符串转化为二进制数组
	 *
	 * @param byteStr
	 * @return
	 * @author wang guang shuai
	 *
	 *         2016年11月19日 下午1:15:44
	 *
	 */
	public static byte[] byteStrToBytes(String byteStr) {
		if (byteStr == null || byteStr.isEmpty()) {
			return null;
		}

		String[] strBytes = byteStr.split(COMMA);
		int length = strBytes.length;
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = Byte.parseByte(strBytes[i]);
		}
		return bytes;
	}

	/**
	 * 
	 * 描述：将二进制转化为二进制的字符串
	 *
	 * @param bytes
	 * @return
	 * @author wang guang shuai
	 *
	 *         2016年11月19日 下午1:19:32
	 *
	 */
	public static String byteToByteStr(byte[] bytes) {
		int length = bytes.length;
		StringBuilder str = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			str.append(bytes[i]).append(COMMA);
		}
		return str.toString();
	}

	/**
	 * 
	 * 描述:这个方法是将一个下划线分隔的字符串转化为首字母大写的驼峰命名字符串
	 *
	 * @param tableName
	 * @return
	 * @author Terry
	 * @time 2016年8月6日-下午4:46:53
	 */
	public static String toCamelCase(String field) {
		String[] strs = field.split(UNDERLINE);
		StringBuilder className = new StringBuilder();
		if (strs.length >= 2) {
			for (String str : strs) {
				className.append(StringUtil.firstToUpper(str));
			}
		} else {

			className.append(StringUtil.firstToUpper(field));
		}
		return className.toString();
	}

	public static void main(String[] args) {
		String str = "abcdefg";

		byte[] bytes = str.getBytes();
		String v = byteToByteStr(bytes);
		System.out.println(v);

		bytes = byteStrToBytes(v);
		System.out.println(new String(bytes));
	}

}
