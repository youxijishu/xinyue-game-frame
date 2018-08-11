package com.xinyue.rocketmq.framework.network;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinyue.network.message.InnerMessageCodecFactory;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelGroupManager;
import com.xinyue.utils.NettyUtil;

import io.netty.buffer.ByteBuf;

public class LogicMessageConsumerListener implements MessageListenerConcurrently {
	private static Logger logger = LoggerFactory.getLogger(LogicMessageConsumerListener.class);
	private GameChannelGroupManager gameChannelGroupManager;
	
	public LogicMessageConsumerListener(GameChannelGroupManager gameChannelGroupManager,
			List<Class<? extends IGameMessage>> gameMessageClasses) {
		this.gameChannelGroupManager = gameChannelGroupManager;
		InnerMessageCodecFactory.getInstance()

				.setGameMessageClass(gameMessageClasses);
	}


	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		if (msgs != null) {
			for (MessageExt msg : msgs) {
				logger.debug("消息者收到消息:{}", msg);
				byte[] body = msg.getBody();
				ByteBuf buf = NettyUtil.createBuf(body.length);
				buf.writeBytes(body);
				try {
					IGameMessage gameMessage = InnerMessageCodecFactory.getInstance().decode(buf);
					this.gameChannelGroupManager.fireReadMessage(gameMessage);
				} catch (Exception e) {
					logger.warn("消息解码错误", e);
				}
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
