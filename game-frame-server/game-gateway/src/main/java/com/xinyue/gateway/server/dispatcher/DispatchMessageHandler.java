package com.xinyue.gateway.server.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.eureka.GlobalEurekaService;
import com.xinyue.eureka.LocalServerInstanceService;
import com.xinyue.gateway.message.GateMessageHeader;
import com.xinyue.gateway.message.IGateMessage;
import com.xinyue.gateway.server.GateMessageRouter;
import com.xinyue.network.message.InnerMessageCodecFactory;
import com.xinyue.network.message.common.GameMessageHead;
import com.xinyue.rocketmq.GameMessageTag;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
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
	private GlobalEurekaService gateEurekaService;
	@Autowired
	private LocalServerInstanceService localServerInstanceService;
	@Autowired
	private GateMessageRouter messageRouter;

	@Autowired

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof IGateMessage) {
			IGateMessage gateMessage = (IGateMessage) msg;
			GateMessageHeader gateMessageHeader = gateMessage.getHeader();
			GameMessageHead messageHead = new GameMessageHead();
			int serverType = gateMessageHeader.getServerType();
			int messageId = gateMessageHeader.getMessageId();
			long roleId = gateMessageHeader.getRoleId();
			int serverId = gateEurekaService.selectServerId(roleId, serverType);
			int localServerId = localServerInstanceService.getLocalServerId();
			messageHead.setFromeServerId((short) localServerId);

			ByteBuf body = gateMessage.getBody();
			// 把从客户端收到的包转化为向业务服务发送的包。
			ByteBuf buf = InnerMessageCodecFactory.getInstance().gateToGameServerEncode(messageHead, body);
			GameMessageTag gameMessageTag = new GameMessageTag(serverType, messageId, serverId);
			String tag = gameMessageTag.getTag();
			messageRouter.sendMessage(roleId, tag, buf);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("服务器异常", cause);
	}
}
