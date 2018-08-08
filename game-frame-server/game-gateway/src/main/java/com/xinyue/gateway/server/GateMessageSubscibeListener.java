package com.xinyue.gateway.server;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import com.xinyue.gateway.service.ChannelService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GateMessageSubscibeListener implements MessageListenerConcurrently {
	private ChannelService channelService;
	
	
	
	public GateMessageSubscibeListener(ChannelService channelService) {
		this.channelService = channelService;
	}
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		for(MessageExt messageExt : msgs){
			long roleId = 0;
			byte[] body = messageExt.getBody();
			ByteBuf buf = Unpooled.wrappedBuffer(body);
			buf.markReaderIndex();
			roleId = buf.readLong();
			buf.resetReaderIndex();
			channelService.writeMessage(roleId, buf);
		}
		System.out.println("网关收到逻辑服务消息：" + msgs.get(0).getBody().length);
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
