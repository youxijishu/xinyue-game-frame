package com.xinyue.utils;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class NettyUtil {

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
}
