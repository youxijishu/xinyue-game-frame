package com.xinyue.network.message.common;

/**
 * 负责消息的解码和编码及验证操作
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年6月10日 下午5:25:04
 */
import java.util.Map;
import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class GameMessageCodecFactory {
	private final static int ResponseHeadLength = 16;
	private Map<Integer, Class<? extends IGameMessage>> messageClassMap = null;
	private Logger logger = LoggerFactory.getLogger(GameMessageCodecFactory.class);

	/**
	 * 
	 * @Desc 设置要解码的消息的id和对应的class
	 * @param commandClassMap
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月10日 下午5:31:03
	 *
	 */
	public void setCommandClass(Map<Integer, Class<? extends IGameMessage>> messageClassMap) {
		this.messageClassMap = messageClassMap;
	}

	/**
	 * 
	 * @Desc 序列化向客户端发送的消息，格式：数据总长度(int(4)) + 消息序列号(int(4)) + 消息id（int(4)) +
	 *       错误码(4) + crc32(8) + protobuf消息体
	 * @param gameMessage
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月10日 下午10:58:36
	 *
	 */
	public ByteBuf encode(IGameMessage gameMessage) throws Exception {
		int total = ResponseHeadLength;
		byte[] cmdBytes = gameMessage.encodeBody();
		if (cmdBytes != null) {
			total += 8;
			total += cmdBytes.length;
		}
		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(total);
		byteBuf.writeInt(total);
		byteBuf.writeInt(gameMessage.getMessageHead().getSeqId());
		byteBuf.writeInt(gameMessage.getMessageHead().getMessageId());
		byteBuf.writeInt(gameMessage.getMessageHead().getErrorCode());
		if (cmdBytes != null) {
			long crcValue = this.getCrcValue(cmdBytes);
			byteBuf.writeLong(crcValue);
			byteBuf.writeBytes(cmdBytes);
		}
		logger.debug("Encode {},字节大小[{}]", gameMessage.getClass().getSimpleName(), total);
		return byteBuf;
	}

	private long getCrcValue(byte[] cmdBytes) {
		CRC32 crc32 = new CRC32();
		crc32.update(cmdBytes);
		long crcValue = crc32.getValue();
		return crcValue;
	}

	/**
	 * 
	 * @Desc 解码客户端的请求包，格式：数据总长度(int(4)) + 消息序列号(int(4)) + 消息id（int(4)) +
	 *       crc32(8) + protobuf消息体
	 * @param byteBuf
	 * @param 上一个请求的序列号
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月10日 下午11:40:14
	 *
	 */
	public IGameMessage decode(ByteBuf byteBuf, int preSeqId) throws Exception {
		int total = byteBuf.readInt();
		logger.debug("接收字节大小:{}", total);
		int seqId = byteBuf.readInt();
		if (seqId <= preSeqId) {
			logger.error("消息seqId不合法,当前seqId:{},接收的seqId:{}", preSeqId, seqId);
			return null;
		}
		int messageId = byteBuf.readInt();
		Class<? extends IGameMessage> clazz = this.messageClassMap.get(messageId);
		if (clazz == null) {
			logger.error("找不到[{}]的GameMessage的实现类", messageId);
			return null;
		}
		byte[] body = null;
		if (byteBuf.readableBytes() > 0) {
			body = new byte[byteBuf.readableBytes()];
			long crcValue = byteBuf.readLong();
			long newCrcValue = this.getCrcValue(body);
			if (crcValue != newCrcValue) {
				logger.error("crc验证失败，收到消息的crcValue:{},计算得到的crcValue:{}", crcValue, newCrcValue);
				return null;
			}
		}
		IGameMessage gameMessage = clazz.newInstance();
		MessageHead messageHead = gameMessage.getMessageHead();
		messageHead.setSeqId(seqId);
		gameMessage.decodeBody(body);
		return gameMessage;
	}

}
