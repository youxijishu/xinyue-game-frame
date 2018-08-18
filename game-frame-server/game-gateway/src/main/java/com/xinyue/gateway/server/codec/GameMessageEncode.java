package com.xinyue.gateway.server.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.message.GateLocalMessage;
import com.xinyue.gateway.message.GateMessageHeader;
import com.xinyue.gateway.message.IGateMessage;
import com.xinyue.network.message.InnerMessageCodecFactory;
import com.xinyue.network.message.impl.GateMessageResponse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 这里处理收到业务服务发送来的消息，加工成返回客户端的包。<br>
 * 返回给客户端的协议格式：total(4) + seqId(4) + uniqueMessageId(4) + errorCode(4) +
 * body(protobuf字节数组)
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午2:25:20
 */
@Service
@Scope(scopeName = "prototype")
public class GameMessageEncode extends ChannelOutboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(GameMessageEncode.class);
	InnerMessageCodecFactory codecFactory = InnerMessageCodecFactory.getInstance();
	// 固定长度
	private final static int Fix_len = 11;

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof GateLocalMessage) {
			GateLocalMessage gateLocalMessage = (GateLocalMessage) msg;
			ByteBuf body = null;
			if (gateLocalMessage.getBody() != null) {
				String json = gateLocalMessage.getBody().toJSONString();
				GateMessageResponse response = new GateMessageResponse();
				response.setResult(json);
				byte[] bytes = response.encodeBody();
				body = Unpooled.wrappedBuffer(bytes);
			}
			ByteBuf buf = encode(gateLocalMessage.getHeader(), body);
			ctx.writeAndFlush(buf);
			promise.setSuccess();
			logger.debug("send to cleint => {}", gateLocalMessage);
		}
		if (msg instanceof IGateMessage) {
			IGateMessage gateMessage = (IGateMessage) msg;

			ByteBuf body = gateMessage.getBody();
			ByteBuf buf = encode(gateMessage.getHeader(), body);
			ctx.writeAndFlush(buf);
			promise.setSuccess();
			logger.debug("send to cleint => {}", gateMessage);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	private ByteBuf encode(GateMessageHeader header, ByteBuf body) {
		int total = Fix_len;
		if (body != null) {
			total += body.readableBytes();
		}
		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(total);
		buf.writeShort(total);
		buf.writeInt(header.getSeqId());
		buf.writeShort(header.getMessageId());
		buf.writeShort(header.getErrorCode());
		buf.writeByte(header.getIsZip());
		buf.writeBytes(body);
		return buf;
	}
}
