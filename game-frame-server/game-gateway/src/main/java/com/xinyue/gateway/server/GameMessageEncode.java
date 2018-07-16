package com.xinyue.gateway.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.network.message.inner.InnerMessageCodecFactory;
import com.xinyue.network.message.inner.InnerMessageHeader;
import com.xinyue.utils.NettyUtil;

import io.netty.buffer.ByteBuf;
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
	private final static int Fix_len = 16;

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

		if (msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			InnerMessageHeader header = codecFactory.getMessageHeaderFromResponse(buf);
			int bodyLen = buf.readableBytes();
			
			byte[] body = null;
			if (bodyLen > 0) {
				
				body = new byte[bodyLen];
				buf.readBytes(body);
			}
			NettyUtil.releaseBuf(buf);
			buf = this.createBytebufToClient(body, header);
			ctx.writeAndFlush(buf);
			promise.setSuccess();
		} else if(msg instanceof IGameMessage){
			IGameMessage gameMessage = (IGameMessage)msg;
		    InnerMessageHeader header = gameMessage.getMessageHead();
		    byte[] body = gameMessage.encodeBody();
		    ByteBuf buf = this.createBytebufToClient(body, header);
		    ctx.writeAndFlush(buf);
		}
	}
	
	private ByteBuf createBytebufToClient(byte[] body,InnerMessageHeader header){
		int total = Fix_len;
		if (body != null){
			int bodyLen = body.length;
			total += bodyLen;
		}
		ByteBuf buf = NettyUtil.createBuf(total);
		buf.writeInt(total);
		buf.writeInt(header.getSeqId());
		buf.writeInt(header.getMessageUniqueId());
		buf.writeInt(header.getErrorCode());
		if (body != null) {
			buf.writeBytes(body);
		}
		logger.debug("<==roleId:{}, message size:{},messageId:{}",header.getRoleId(), total, header.getMessageId());
		return buf;
	}
}
