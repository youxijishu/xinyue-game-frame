package com.xinyue.gateway.server;

import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.server.model.GateMessageInfo;
import com.xinyue.gateway.utils.NettyUtil;
import com.xinyue.network.message.common.GameMessageRegisterFactory;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.network.message.common.MessageHead;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 解码从客户端收到的数据包，并负责对包的正确性进行验证，在这里消息被检测是否需要网关处理整个消息，即解码出整个消息的内容，
 * 由网关自己处理，不需要转发到业务服务，如果不需要网关处理，则把消息体不需要解码，直接发送到下一个handler去处理
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
	private static byte[] CrcFactor = null;
	static {
		String msg = "xinyue";
		CrcFactor = msg.getBytes(CharsetUtil.UTF_8);
	}

	/**
	 * 客户端消息协议：数据总长度(int(4)) + 消息序列号(int(4)) + 消息id（int(4)) + crc32(8) +
	 * protobuf消息体
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg == null) {
			return;
		}
		ByteBuf byteBuf = (ByteBuf) msg;
		int total = byteBuf.readInt();
		logger.debug("ip:{},channelId:{},接收字节大小:{}", total, NettyUtil.getIp(ctx), NettyUtil.getChannelId(ctx));
		MessageHead messageHead = new MessageHead();
		messageHead.readMessage(byteBuf);
		// 验证序列号是否正确
		if (messageHead.getSeqId() <= nowSeqId) {
			logger.warn("收到消息重复，消息非法，ip:{},channelId:{}", NettyUtil.getIp(ctx), NettyUtil.getChannelId(ctx));
			ctx.close();
			return;
		}
		byte[] body = null;
		if (byteBuf.readableBytes() > 0) {
			body = new byte[byteBuf.readableBytes()];
			// 验证消息的crc是否正确
			long crcValue = byteBuf.readLong();
			long newCrcValue = this.getCrcValue(body);
			if (crcValue != newCrcValue) {
				logger.error("crc验证失败，收到消息的crcValue:{},计算得到的crcValue:{},ip:{},channelId:{}", crcValue, newCrcValue,
						NettyUtil.getIp(ctx), NettyUtil.getChannelId(ctx));
				ctx.close();
				return;
			}
		}

		int messageId = messageHead.getMessageId();
		// 判断一下，这个命令是不是gate服务直接处理的，如果是就不转发了。
		boolean isExistGateMessage = GameMessageRegisterFactory.getInstance().containsGameMessage(messageId);
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
			ctx.fireChannelRead(gateMessageInfo);
		}
	}
	/**
	 * 
	 * @Desc  计算包体的crc值
	 * @param body
	 * @return
	 * @Author 心悦网络  王广帅
	 * @Date 2018年6月14日 下午3:41:40
	 *
	 */
	private long getCrcValue(byte[] body) {
		CRC32 crc32 = new CRC32();
		crc32.update(body);
		crc32.update(CrcFactor);
		long crcValue = crc32.getValue();
		return crcValue;
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
		int messageId = messageHead.getMessageId();
		GameMessageRegisterFactory gameMessageCodecFactory = GameMessageRegisterFactory.getInstance();
		IGameMessage gameMessage = gameMessageCodecFactory.getGameMessage(messageId);
		if (gameMessage == null) {
			logger.error("找不到[{}]的GameMessage的实现类", messageId);
			return null;
		}
		MessageHead messageHead2 = gameMessage.getMessageHead();
		messageHead2.setSeqId(messageHead.getSeqId());
		gameMessage.decodeBody(body);
		return gameMessage;
	}
}
