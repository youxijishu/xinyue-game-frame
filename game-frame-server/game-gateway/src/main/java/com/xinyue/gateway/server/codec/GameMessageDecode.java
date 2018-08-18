package com.xinyue.gateway.server.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinyue.gateway.message.GateLocalMessage;
import com.xinyue.gateway.message.GateMessage;
import com.xinyue.gateway.message.GateMessageHeader;
import com.xinyue.gateway.server.confirm.GameGatewayConnectHandler;
import com.xinyue.gateway.server.model.GateUserInfo;
import com.xinyue.network.EnumServerType;
import com.xinyue.network.message.impl.GateMessageRequest;
import com.xinyue.utils.NettyUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 解码从客户端接收的消息，详细见<a href =
 * "https://www.cnblogs.com/wgslucky/p/9130993.html">网关的实现</a>
 * 
 * @Date 2018年6月14日 下午3:17:46
 */
@Service
@Scope(scopeName = "prototype")
public class GameMessageDecode extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(GameMessageDecode.class);
	private String ip;

	private final static int gateLocalMessageId = 1001;
	private final GateMessageRequest gateMessageRequest = new GateMessageRequest();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ip = NettyUtil.getIp(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf byteBuf = (ByteBuf) msg;
		try {
			short total = byteBuf.readShort();
			logger.debug("ip:{},channelId:{},接收字节大小:{}", NettyUtil.getIp(ctx), NettyUtil.getChannelId(ctx), total);

			int seqId = byteBuf.readInt();
			short messageId = byteBuf.readShort();
			long receiveTime = byteBuf.readLong();
			short serverType = byteBuf.readShort();
			byte isZip = byteBuf.readByte();
			ByteBuf body = byteBuf.slice();
			GateMessageHeader gateMessageHeader = new GateMessageHeader();
			gateMessageHeader.setSeqId(seqId);
			gateMessageHeader.setServerType(serverType);
			gateMessageHeader.setIsZip(isZip);
			gateMessageHeader.setMessageId(messageId);
			gateMessageHeader.setReceiveTime(receiveTime);
			gateMessageHeader.setIp(ip);
			ChannelHandler channelHandler = ctx.channel().pipeline().get(GameGatewayConnectHandler.class.getName());
			if (channelHandler != null) {
				GameGatewayConnectHandler gameGatewayConnectHandler = (GameGatewayConnectHandler) channelHandler;
				GateUserInfo gateUserInfo = gameGatewayConnectHandler.getGateUserInfo();
				if (gateUserInfo != null) {
					gateMessageHeader.setRoleId(gateUserInfo.getRoleId());
					gateMessageHeader.setUserId(gateUserInfo.getUserId());
				}
			}
			if (messageId == gateLocalMessageId && serverType == EnumServerType.GATE.getServerType()) {
				GateLocalMessage gateLocalMessage = new GateLocalMessage();
				gateLocalMessage.setHeader(gateMessageHeader);
				JSONObject result = this.decode(byteBuf);
				gateLocalMessage.setBody(result);
				ctx.fireChannelRead(gateLocalMessage);
			} else {
				GateMessage gateMessage = new GateMessage();
				gateMessage.setBody(body);
				gateMessage.setHeader(gateMessageHeader);
				ctx.fireChannelRead(gateMessage);
				logger.debug("Gate Recieve => {}", gateMessage);
			}

		} finally {
			ReferenceCountUtil.release(byteBuf);
		}
	}

	public JSONObject decode(ByteBuf buf) throws Exception {
		if (buf.readableBytes() > 0) {
			byte[] body = new byte[buf.readableBytes()];
			buf.readBytes(body);
			gateMessageRequest.decodeBody(body);
			String json = gateMessageRequest.getMessage();
			JSONObject result = JSON.parseObject(json);
			return result;
		}

		return null;
	}
}
