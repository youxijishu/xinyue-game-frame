package com.xinyue.network.message.inner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinyue.network.EnumServerType;
import com.xinyue.network.message.common.GameMessageRegisterFactory;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.utils.IpUtil;
import com.xinyue.utils.NettyUtil;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

/**
 * 内部消息包的编码与解码：<br>
 * 网关给业务服务的编码格式：messageUniqueId(4) + userId(8) + roleId(8) + seqId(4)
 * + fromServerId(4) + toServerId(4) + isIpv4(1,0表示是ipv4,1是ipv6) +
 * ip(如果是ipv4占4位,如果是ipv6，2位的字符串长度，变长的ipv6字符串长度，后期可以优化为两个long) + body <br>
 * 业务服务给网关的消息编码：serverType(2) + messageId(2) + userId(8) + roleId(8) + seqId(4)
 * + errorCode(4) + body;
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午11:03:05
 */
public class InnerMessageCodecFactory {
	private Logger logger = LoggerFactory.getLogger(InnerMessageCodecFactory.class);
	private static InnerMessageCodecFactory instance = new InnerMessageCodecFactory();
	private GameMessageRegisterFactory registerFactory = GameMessageRegisterFactory.getInstance();

	public static InnerMessageCodecFactory getInstance() {
		return instance;
	}

	public void setGameMessageClass(List<Class<? extends IGameMessage>> gameMessages) {
		registerFactory.registerCommandClass(gameMessages);
	}

	/**
	 * 
	 * @Desc 把网关发给业务服务器的消息进行编码。
	 * @param innerMessageHeader
	 * @param body
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午11:14:26
	 *
	 */
	public ByteBuf gateEncode(InnerMessageHeader innerMessageHeader, byte[] body) {
		// 前面几个固定的长度和
		int total = 33;
		// 下面开始计算整个包的大小
		String ip = innerMessageHeader.getClientIp();
		boolean isIpv4 = IpUtil.Isipv4(ip);
		byte[] ipv6Byte = null;
		if (isIpv4) {
			total += 4;
		} else {
			ipv6Byte = ip.getBytes(CharsetUtil.UTF_8);
			total += 2;
			total += ipv6Byte.length;
		}
		if (body != null) {
			total += body.length;
		}
		ByteBuf buf = NettyUtil.createBuf(total);
		int messageUniqueId = innerMessageHeader.getMessageUniqueId();
		buf.writeInt(messageUniqueId);
		buf.writeLong(innerMessageHeader.getUserId());
		buf.writeLong(innerMessageHeader.getRoleId());
		buf.writeInt(innerMessageHeader.getSeqId());
		buf.writeInt(innerMessageHeader.getFromServerId());
		buf.writeInt(innerMessageHeader.getToServerId());

		if (isIpv4) {
			buf.writeByte(0);
			buf.writeInt(IpUtil.ipToInt(ip));
		} else {
			buf.writeByte(1);
			buf.writeShort(ipv6Byte.length);
			buf.writeBytes(ipv6Byte);
		}
		if (body != null) {
			buf.writeBytes(body);
		}
		return buf;
	}

	/**
	 * 
	 * @Desc 当业务服务接收网关过来的消息之后，进行解码
	 * @param buf
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午11:09:17
	 *
	 */
	public IGameMessage decode(ByteBuf buf) throws Exception {
		IGameMessage gameMessage;
		try {
			int total = buf.readableBytes();
			int messageUniqueId = buf.readInt();
			long userId = buf.readLong();
			long roleId = buf.readLong();
			int seqId = buf.readInt();
			int fromServerId = buf.readInt();
			int toServerId = buf.readInt();
			gameMessage = registerFactory.getGameMessage(messageUniqueId);
			if (gameMessage == null) {
				return null;
			}
			byte isIpv4 = buf.readByte();
			String ip = null;
			if (isIpv4 == 0) {
				int ipv4Int = buf.readInt();
				ip = IpUtil.intToIP(ipv4Int);
			} else {
				short ipv6Len = buf.readShort();
				byte[] ipv6Bytes = new byte[ipv6Len];
				buf.readBytes(ipv6Bytes);
				ip = new String(ipv6Bytes, CharsetUtil.UTF_8);
			}
			InnerMessageHeader header = gameMessage.getMessageHead();
			header.setClientIp(ip);
			header.setRecTime(System.currentTimeMillis());
			header.setRoleId(roleId);
			header.setSeqId(seqId);
			header.setUserId(userId);
			header.setFromServerId(fromServerId);
			header.setToServerId(toServerId);
			int bodyLen = buf.readableBytes();
			if (bodyLen > 0) {
				byte[] body = new byte[bodyLen];
				buf.readBytes(body);
				gameMessage.decodeBody(body);
			}
			logger.debug("==> message size [{}],message class :{},param:{}", total,
					gameMessage.getClass().getSimpleName(), gameMessage);
		} finally {
			NettyUtil.releaseBuf(buf);
		}
		return gameMessage;
	}

	/**
	 * 
	 * @Desc 当业务服务给网关返回消息的时候，进行编码
	 * @param gameMessage
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午11:09:54
	 *
	 */
	public byte[] encode(IGameMessage gameMessage) throws Exception {
		// 固定长度
		int total = 28;
		byte[] body = gameMessage.encodeBody();
		if (body != null) {
			total += body.length;
		}
		ByteBuf buf = NettyUtil.createBuf(total);
		try {
			InnerMessageHeader header = gameMessage.getMessageHead();
			buf.writeLong(header.getRoleId());
			buf.writeLong(header.getUserId());
			buf.writeShort(header.getServerType().getServerType());
			buf.writeShort(header.getMessageId());
			buf.writeInt(header.getSeqId());
			buf.writeInt(header.getErrorCode());
			if (header.getErrorCode() == 0) {
				if (body != null) {
					buf.writeBytes(body);
				}
			}
			body = new byte[total];
			buf.readBytes(body);
		} finally {
			NettyUtil.releaseBuf(buf);
		}
		return body;
	}

	/**
	 * 
	 * @Desc 从业务服务向网关返回的消息中获取消息的包头
	 * @param buf
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月15日 下午4:54:31
	 *
	 */
	public InnerMessageHeader getMessageHeaderFromResponse(ByteBuf buf) {
		InnerMessageHeader header = new InnerMessageHeader();
		long roleId = buf.readLong();
		long userId = buf.readLong();
		short serverType = buf.readShort();
		short messageId = buf.readShort();
		int seqId = buf.readInt();
		int errorCode = buf.readInt();
		header.setServerType(EnumServerType.getServerType(serverType));
		header.setMessageId(messageId);
		header.setUserId(userId);
		header.setRoleId(roleId);
		header.setSeqId(seqId);
		header.setErrorCode(errorCode);
		return header;
	}

}
