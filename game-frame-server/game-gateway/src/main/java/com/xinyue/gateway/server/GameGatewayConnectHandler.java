package com.xinyue.gateway.server;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GameGatewayConnectHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(GameGatewayConnectHandler.class);
	private static AtomicInteger channelCount = new AtomicInteger();
	private String clientIp;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.clientIp = getIp(ctx);
		logger.debug("新建连接，clientIp:{},当前连接数:[{}]", clientIp, channelCount.incrementAndGet());
		
	}

	private String getIp(ChannelHandlerContext ctx) {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().unsafe().remoteAddress();
		return inetSocketAddress.getAddress().getHostAddress();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("连接关闭，clientIp:{}", clientIp);
		channelCount.decrementAndGet();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

	}
}
