package com.xinyue.network.message.inner;

import java.util.List;

import com.xinyue.network.message.common.GameMessageRegisterFactory;
import com.xinyue.network.message.common.IGameMessage;

import io.netty.buffer.ByteBuf;

/**
 * 内部消息包的编码与解码：<br>
 * 网关给业务服务的编码格式：userId(8) + roleId(8) + token(64) + grecTime(8) + ip(4) + body   <br>
 * 业务服务给网关的消息编码：userId + roleId + grecTime + lrecTime + errorCode + body;
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午11:03:05
 */
public class InnerMessageCodecFactory {

	private static InnerMessageCodecFactory instance = new InnerMessageCodecFactory();
	private GameMessageRegisterFactory registerFactory = GameMessageRegisterFactory.getInstance();

	public static InnerMessageCodecFactory getInstance() {
		return instance;
	}

	public void setGameMessageClass(List<Class<? extends IGameMessage>> gameMessages) {
		registerFactory.registerCommandClass(gameMessages);
	}

	public InnerMessageHeader getMessageHeader(ByteBuf buf) {
		
		return null;
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
		
		return null;
	}

	/**
	 * 
	 * @Desc 当业务服务接收网关过来的消息之后，进行解码
	 * @param buf
	 * @param header
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午11:09:17
	 *
	 */
	public IGameMessage decode(ByteBuf buf, InnerMessageHeader header) {

		return null;
	}

	/**
	 * 
	 * @Desc 当业务服务给网关返回消息的时候，进行编码
	 * @param gameMessage
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午11:09:54
	 *
	 */
	public byte[] encode(IGameMessage gameMessage) {

		return null;
	}

}
