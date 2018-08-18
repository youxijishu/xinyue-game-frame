package com.xinyue.gateway.utils;

import io.netty.channel.ChannelHandlerContext;

public class ChannelUtil {

	
	public static String getChannelId(ChannelHandlerContext ctx){
		return ctx.channel().id().asShortText();
	}
}
