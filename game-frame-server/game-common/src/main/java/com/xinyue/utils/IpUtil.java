package com.xinyue.utils;

public class IpUtil {
	/**
	 * ip地址转成int型数字 将IP地址转化成整数的方法如下： 1、通过String的split方法按.分隔得到4个长度的数组
	 * 2、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
	 * 
	 * @param strIp
	 * @return
	 */
	public static int ipToInt(String strIp) {
		String[] ip = strIp.split("\\.");
		return (Integer.parseInt(ip[0]) << 24) + (Integer.parseInt(ip[1]) << 16) + (Integer.parseInt(ip[2]) << 8)
				+ Integer.parseInt(ip[3]);
	}

	/**
	 * 将十进制整数形式转换成127.0.0.1形式的ip地址 将整数形式的IP地址转化成字符串的方法如下：
	 * 1、将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。
	 * 2、通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。
	 * 3、通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。
	 * 4、通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。
	 * 
	 * @param longIp
	 * @return
	 */
	public static String intToIP(int longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	public static boolean Isipv4(String ipv4) {
		if (ipv4 == null || ipv4.length() == 0) {
			return false;// 字符串为空或者空串
		}
		String[] parts = ipv4.split("//.");
		// 因为java doc里已经说明, split的参数是reg, 即正则表达式, 如果用"|"分割, 则需使用"//|"
		if (parts.length != 4) {
			return false;
		}
		// 分割开的数组根本就不是4个数字
		for (int i = 0; i < parts.length; i++) {
			try {
				int n = Integer.parseInt(parts[i]);
				if (n < 0 || n > 255) {
					return false;// 数字不在正确范围内
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {

	}

}
