package com.xinyue.network.message;

import java.util.List;

import com.xinyue.network.message.common.GameMessageHead;
import com.xinyue.network.message.common.GameMessageRegisterFactory;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.utils.StringUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * 内部消息包的编码与解码：<br>
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午11:03:05
 */
public class InnerMessageCodecFactory {
	private static InnerMessageCodecFactory instance = new InnerMessageCodecFactory();
	private GameMessageRegisterFactory registerFactory = GameMessageRegisterFactory.getInstance();

	/**
	 * roleId （long） userId (long) 消息序列号(int) 消息Id (short) 消息发送时间（long)
	 * 消息来源的服务实例Id (short) 消息要到达的服务实例id (short) 错误码（short）
	 */

	private static final short Fix_Len = 8 + 8 + 4 + 2 + 2 + 8 + 2 + 2 + 2;

	public static InnerMessageCodecFactory getInstance() {
		return instance;
	}

	public void setGameMessageClass(List<Class<? extends IGameMessage>> gameMessages) {
		registerFactory.registerCommandClass(gameMessages);
	}

	/**
	 * 
	 * @Desc 编码从网关到业务服务的消息,serverType不需要传送，在Message的实例中是可以找到的。
	 * @param messageHead
	 * @param body
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年8月12日 上午12:14:22
	 *
	 */
	public ByteBuf gateToGameServerEncode(GameMessageHead messageHead, ByteBuf body) {
		int bodySize = 0;
		if (body != null) {
			bodySize = body.readableBytes();
		}
		ByteBuf byteBuf = this.writeMessageHeader(messageHead, bodySize);
		if (body != null) {
			byteBuf.writeBytes(body);
		}
		return byteBuf;
	}

	private ByteBuf writeMessageHeader(GameMessageHead messageHead, int bodySize) {
		int initialCapacity = Fix_Len + messageHead.getIp().length() + bodySize;
		ByteBuf byteBuf = Unpooled.buffer(initialCapacity);
		byteBuf.writeLong(messageHead.getRoleId());
		byteBuf.writeLong(messageHead.getUserId());
		byteBuf.writeInt(messageHead.getSeqId());
		byteBuf.writeShort(messageHead.getMessageId());
		byteBuf.writeLong(messageHead.getSendTime());
		byteBuf.writeShort(messageHead.getFromeServerId());
		byteBuf.writeShort(messageHead.getToServerId());
		byteBuf.writeShort(messageHead.getErrorCode());

		if (!StringUtil.isNullEmpty(messageHead.getIp())) {
			byteBuf.writeShort(messageHead.getIp().length());
			byteBuf.writeBytes(messageHead.getIp().getBytes(CharsetUtil.UTF_8));
		} else {
			byteBuf.writeShort(0);
		}
		return byteBuf;
	}

	public GameMessageHead readGameMessageHead(ByteBuf buf) {
		GameMessageHead gameMessageHead = new GameMessageHead();
		gameMessageHead.setRoleId(buf.readLong());
		gameMessageHead.setUserId(buf.readLong());
		gameMessageHead.setSeqId(buf.readInt());
		gameMessageHead.setMessageId(buf.readShort());
		gameMessageHead.setSendTime(buf.readLong());
		gameMessageHead.setFromeServerId(buf.readShort());
		gameMessageHead.setToServerId(buf.readShort());
		gameMessageHead.setErrorCode(buf.readShort());
		int ipLen = buf.readShort();
		if (ipLen > 0) {
			byte[] ipBytes = new byte[ipLen];
			buf.readBytes(ipBytes);
			gameMessageHead.setIp(new String(ipBytes, CharsetUtil.UTF_8));
		}

		return gameMessageHead;
	}

	/**
	 * 
	 * @Desc 编码从业务服务的消息
	 * @param gameMessage
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年8月12日 上午12:15:17
	 *
	 */
	public ByteBuf gameServerMessageEncode(IGameMessage gameMessage) throws Exception {
		byte[] body = gameMessage.encodeBody();
		int bodySize = 0;
		if (body != null) {
			bodySize = body.length;
		}
		ByteBuf byteBuf = this.writeMessageHeader(gameMessage.getMessageHead(), bodySize);
		if (body != null) {
			byteBuf.writeBytes(body);
		}
		return byteBuf;

	}

	/**
	 * 
	 * @Desc 解码业务服务消息
	 * @param body
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年8月12日 上午12:17:47
	 *
	 */
	public IGameMessage gameServerMessageDecode(byte[] body) throws Exception {
		ByteBuf byteBuf = Unpooled.wrappedBuffer(body);
		GameMessageHead messageHead = this.readGameMessageHead(byteBuf);
		IGameMessage gameMessage = registerFactory.getGameMessage(messageHead.getMessageId(),
				messageHead.getServerType());
		if (gameMessage != null) {
			gameMessage.setMessageHead(messageHead);
			byte[] bytes = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(bytes);
			gameMessage.decodeBody(bytes);
		}
		return gameMessage;
	}

}
