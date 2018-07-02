package com.xinyue.gateway.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.server.model.GateMessageInfo;
import com.xinyue.network.EnumServerType;
import com.xinyue.network.message.common.GameMessageRegisterFactory;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.network.message.common.MessageHead;
import com.xinyue.network.message.common.MessageIdUtil;
import com.xinyue.utils.NettyUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 解码从客户端收到的数据包，并负责对包的正确性进行验证，在这里消息被检测是否需要网关处理整个消息，即解码出整个消息的内容，
 * 由网关自己处理，不需要转发到业务服务，如果不需要网关处理，则把消息体不需要解码，直接发送到下一个handler去处理 <br>
 * 客户端发送到网关的消息格式为：total(4) + seqId(4) + messageUniqueId(4) + body(protobuf字节数组)
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午3:17:46
 */
@Service
@Scope(scopeName = "prototype")
public class GameMessageDecode extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(GameMessageDecode.class);
	private int nowSeqId = 0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg == null) {
			return;
		}

		ByteBuf byteBuf = (ByteBuf) msg;
		try {
			int total = byteBuf.readInt();
			logger.debug("ip:{},channelId:{},接收字节大小:{}",  NettyUtil.getIp(ctx), NettyUtil.getChannelId(ctx),total);
			MessageHead messageHead = new MessageHead();
			int seqId = byteBuf.readInt();
			messageHead.setSeqId(seqId);
			// 验证序列号是否正确
			if (messageHead.getSeqId() <= nowSeqId) {
				logger.warn("收到消息重复，消息非法，ip:{},channelId:{}", NettyUtil.getIp(ctx), NettyUtil.getChannelId(ctx));
				ctx.close();
				return;
			}
			int messageUniqueId = byteBuf.readInt();
			short serverType = MessageIdUtil.getServerType(messageUniqueId);
			short messageId = MessageIdUtil.getMessageId(messageUniqueId);
			messageHead.setServerType(EnumServerType.getServerType(serverType));
			messageHead.setMessageId(messageId);
			byte[] body = null;
			if (byteBuf.readableBytes() > 0) {
				body = new byte[byteBuf.readableBytes()];
				byteBuf.readBytes(body);
			}
			// 判断一下，这个命令是不是gate服务直接处理的，如果是就不转发了。
			boolean isExistGateMessage = GameMessageRegisterFactory.getInstance().containsGameMessage(messageUniqueId);
			if (isExistGateMessage) {
				IGameMessage gameMessage = this.decode(messageHead, body);
				if (gameMessage != null) {
					ctx.fireChannelRead(gameMessage);
				} else {
					logger.warn("非法请求，关闭连接,ip:{},channelId:{}", NettyUtil.getIp(ctx), ctx.channel().id().asShortText());
					ctx.close();
				}
			} else {
				// 把包头和包体封装一下，发送到下个handler去处理。
				GateMessageInfo gateMessageInfo = new GateMessageInfo(messageHead, body);
				gateMessageInfo.setMessageTotalSize(total);
				ctx.fireChannelRead(gateMessageInfo);
			}
		} finally {
			ReferenceCountUtil.release(byteBuf);
		}
	}

	/**
	 * 
	 * @param byteBuf
	 * @param 上一个请求的序列号
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月10日 下午11:40:14
	 *
	 */
	public IGameMessage decode(MessageHead messageHead, byte[] body) throws Exception {
		GameMessageRegisterFactory gameMessageCodecFactory = GameMessageRegisterFactory.getInstance();
		IGameMessage gameMessage = gameMessageCodecFactory.getGameMessage(messageHead.getMessageUniqueId());
		if (gameMessage == null) {
			return null;
		}
		MessageHead messageHead2 = gameMessage.getMessageHead();
		messageHead2.setSeqId(messageHead.getSeqId());
		gameMessage.decodeBody(body);
		return gameMessage;
	}
}
