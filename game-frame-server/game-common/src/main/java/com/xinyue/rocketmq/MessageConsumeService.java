package com.xinyue.rocketmq;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import com.xinyue.rocketmq.config.MessageConsumeConfig;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MessageConsumeService {

	public MessageConsumeConfig consumeConfig;
	private DefaultMQPushConsumer consumer;

	public void start(MessageConsumeConfig consumeConfig, MessageReceiveListener receiveListener)
			throws MQClientException {
		consumer = new DefaultMQPushConsumer(consumeConfig.getGroupName());
		consumer.setInstanceName(consumeConfig.getInstanceName());
		// consumer.setNamesrvAddr(rocketmqConfig.getNameServerAddr());
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (MessageExt messageExt : msgs) {
					byte[] body = messageExt.getBody();
					ByteBuf byteBuf = Unpooled.wrappedBuffer(body);
					receiveListener.receive(byteBuf);

				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		String allTags = combineTags(consumeConfig.getTags());
		consumer.subscribe(consumeConfig.getTopic(), allTags);
		consumer.start();
	}

	private String combineTags(List<String> tags) {
		if (tags == null) {
			throw new IllegalArgumentException("tags 不能为null");
		}
		if (tags.size() == 1) {
			return tags.get(0);
		}
		StringBuilder allTags = new StringBuilder();
		for (String tag : tags) {
			allTags.append(tag).append(" || ");
		}
		if (allTags.length() < 4) {
			return null;
		}
		allTags.delete(allTags.length() - 4, allTags.length());
		return allTags.toString();
	}
}
