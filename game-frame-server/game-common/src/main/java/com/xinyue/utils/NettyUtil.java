package com.xinyue.utils;

import java.net.InetSocketAddress;
import java.util.zip.CRC32;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class NettyUtil {
	private static byte[] CrcFactor = null;
	static {
		String msg = "xinyue";
		CrcFactor = msg.getBytes(CharsetUtil.UTF_8);
	}

	public static String getIp(ChannelHandlerContext ctx) {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().unsafe().remoteAddress();
		return inetSocketAddress.getAddress().getHostAddress();
	}

	public static ByteBuf createBuf(int capacity) {
		return ByteBufAllocator.DEFAULT.buffer(capacity);
	}

	public static void releaseBuf(ByteBuf byteBuf) {
		ReferenceCountUtil.release(byteBuf);
	}

	public static String getChannelId(ChannelHandlerContext ctx) {
		return ctx.channel().id().asShortText();
	}

	/**
	 * 
	 * @Desc 计算包体的crc值
	 * @param body
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午3:41:40
	 *
	 */
	public static long getCrcValue(byte[] body) {
		CRC32 crc32 = new CRC32();
		crc32.update(body);
		crc32.update(CrcFactor);
		long crcValue = crc32.getValue();
		return crcValue;
	}
}
