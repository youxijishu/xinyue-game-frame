package com.xinyue.gateway.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.message.GateMessageHeader;
import com.xinyue.gateway.message.IGateMessage;
import com.xinyue.network.message.inner.InnerMessageCodecFactory;
import com.xinyue.utils.NettyUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
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

		if (msg instanceof IGateMessage) {
			IGateMessage gateMessage = (IGateMessage) msg;
			int total = Fix_len;
			ByteBuf body = gateMessage.getBody();
			try {
				if (body != null) {
					total += body.readableBytes();
				}
				ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(total);
				GateMessageHeader header = gateMessage.getHeader();
				buf.writeShort(total);
				buf.writeInt(header.getSeqId());
				buf.writeShort(header.getMessageId());
				buf.writeShort(header.getErrorCode());
				buf.writeByte(header.getIsZip());
				buf.writeBytes(body);
				ctx.writeAndFlush(buf);
				promise.setSuccess();
				logger.debug("send to cleint => {}",gateMessage);
			} finally {
				if(body != null){
					NettyUtil.releaseBuf(body);
				}
			}
			
		} else {
			ctx.fireChannelRead(msg);
		}
	}
}
