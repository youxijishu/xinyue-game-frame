package com.xinyue.gateway.utils;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;

public class NettyUtil {

	public static String getIp(ChannelHandlerContext ctx) {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().unsafe().remoteAddress();
		return inetSocketAddress.getAddress().getHostAddress();
	}
	
	public static String getChannelId(ChannelHandlerContext ctx){
		return ctx.channel().id().asShortText();
	}
}
