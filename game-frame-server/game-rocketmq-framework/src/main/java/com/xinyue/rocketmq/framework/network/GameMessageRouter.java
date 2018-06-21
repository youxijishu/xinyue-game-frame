package com.xinyue.rocketmq.framework.network;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;

import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.network.message.inner.InnerMessageCodecFactory;
import com.xinyue.rocketmq.RocketmqConfig;

public class GameMessageRouter {
	private DefaultMQProducer producer;
	private DefaultMQPushConsumer consumer;
	private RocketmqConfig rocketmqConfig;

	public void init(RocketmqConfig rocketmqConfig,MessageListenerConcurrently messageListener, String[] tags) throws MQClientException {
		this.rocketmqConfig = rocketmqConfig;
		producer = new DefaultMQProducer(rocketmqConfig.getPublishGroupName());
		producer.setNamesrvAddr(rocketmqConfig.getNameServerAddr());
		producer.setInstanceName(rocketmqConfig.getInstanceName());
		producer.start();

		consumer = new DefaultMQPushConsumer(rocketmqConfig.getConsumerGroupName());
		consumer.setInstanceName(rocketmqConfig.getInstanceName());
		consumer.registerMessageListener(messageListener);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		String allTags = combineTags(tags);
		consumer.subscribe(rocketmqConfig.getConsumerTopic(), allTags);
	}

	private String combineTags(String[] tags) {
		StringBuilder allTags = new StringBuilder();
		for (String tag : tags) {
			allTags.append(tag).append(" || ");
		}
		allTags.delete(allTags.length() - 4, allTags.length());
		return allTags.toString();
	}

	public void sendMessage(IGameMessage gameMessage) throws Exception {
		byte[] body = InnerMessageCodecFactory.getInstance().encode(gameMessage);
		String tag = gameMessage.getMessageHead().getToGateServerMessageTag();
		Message msg = new Message(rocketmqConfig.getPublishTopic(), tag, body);
		producer.sendOneway(msg);
	}
}
