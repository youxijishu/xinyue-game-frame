package com.xinyue.gateway.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.config.ServerConfig;
import com.xinyue.network.message.impl.ConnectConfirmRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

@Service
@Scope(scopeName = "prototype")
public class GameGatewayConnectHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(GameGatewayConnectHandler.class);
	private static AtomicInteger channelCount = new AtomicInteger();
	private String clientIp;
	private ScheduledFuture<?> waitConnectConfirmFuture = null;
	private ConnectConfirmRequest connectConfirmRequest;
	@Autowired
	private ServerConfig serverConfig;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.clientIp = getIp(ctx);
		String channelId = ctx.channel().id().asShortText();
		logger.debug("新建连接{}，clientIp:{},当前连接数:[{}]", channelId, clientIp, channelCount.incrementAndGet());
		int timeout = serverConfig.getConnectTimeout();
		waitConnectConfirmFuture = ctx.executor().schedule(() -> {
			if (connectConfirmRequest == null) {
				logger.warn("客户端：{} 连接成功之后没有认证，关闭连接:{}", clientIp, channelId);
				ctx.close();
			}
		}, timeout, TimeUnit.SECONDS);
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
