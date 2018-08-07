package com.xinyue.gateway.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.config.ServerConfig;
import com.xinyue.gateway.server.model.GateMessageInfo;
import com.xinyue.gateway.service.ILogicServerService;
import com.xinyue.network.message.inner.InnerMessageCodecFactory;
import com.xinyue.network.message.inner.InnerMessageHeader;
import com.xinyue.utils.NettyUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 这里负责对客户端消息的组合和转发。把它变成内部消息，并发布到消息队列中。每个GameMessage都有一个ServerType的元数据。一个ServerType可能部署多组
 * 所以，这是一个一对多的关系。当用户收到GameMessage之后，会根据ServerType确定消息去哪种类型的服务，如果这种服务部署了多个，则使用role
 * % 数据数量的算法 找到一个处理角色消息的服务，并缓存。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午6:22:07
 */
@Service
@Scope(scopeName = "prototype")
public class DispatchMessageHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(DispatchMessageHandler.class);
	InnerMessageCodecFactory codecFactory = InnerMessageCodecFactory.getInstance();
	@Autowired
	private ILogicServerService logicServerService;
	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private GateGameMessageRouter gameMessageRouter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof GateMessageInfo) {
			GateMessageInfo messageInfo = (GateMessageInfo) msg;
			byte[] body = messageInfo.getBody();
			InnerMessageHeader header = new InnerMessageHeader();
			header.setClientIp(NettyUtil.getIp(ctx));
			header.setMessageId(messageInfo.getMessageHead().getMessageId());
			header.setRoleId(messageInfo.getRoleId());
			header.setUserId(messageInfo.getUserId());
			header.setSeqId(messageInfo.getMessageHead().getSeqId());
			header.setServerType(messageInfo.getMessageHead().getServerType());
			header.setFromServerId(serverConfig.getServerId());
			// 计算toServerId
			short serverId = logicServerService.getToServerId(header.getRoleId(), header.getServerType());
			header.setToServerId(serverId);
			ByteBuf buf = codecFactory.gateEncode(header, body);
			// 这里使用消息队列向业务服务发送消息。
			String tag = header.getToLogicServerMessageTag();
			
			body = new byte[buf.readableBytes()];
			buf.readBytes(body);
			gameMessageRouter.sendMessage(body, tag);
			logger.debug("send messge->serverId: {},messageId: {},serverType: {}",serverId,messageInfo.getMessageHead().getMessageId(),messageInfo.getMessageHead().getServerType());
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("服务器异常",cause);
	}
}
