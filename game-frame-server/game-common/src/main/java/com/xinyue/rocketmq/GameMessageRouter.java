package com.xinyue.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;

public abstract class GameMessageRouter {
	private DefaultMQProducer producer;
	private DefaultMQPushConsumer consumer;
	private RocketmqConfig rocketmqConfig;

	public void start() throws MQClientException {
		System.setProperty("rocketmq.client.log.loadconfig", "false");
		this.rocketmqConfig = getRocketmqConfig();
		producer = new DefaultMQProducer(rocketmqConfig.getPublishGroupName());
		producer.setNamesrvAddr(rocketmqConfig.getNameServerAddr());
		producer.setInstanceName(rocketmqConfig.getInstanceName());
		producer.start();
		// ----------------------------------------------------//
		consumer = new DefaultMQPushConsumer(rocketmqConfig.getConsumerGroupName());
		consumer.setInstanceName(rocketmqConfig.getInstanceName());
		consumer.setNamesrvAddr(rocketmqConfig.getNameServerAddr());
		consumer.registerMessageListener(getMessageListener());
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		String allTags = combineTags(getAllListenerTags());
		consumer.subscribe(rocketmqConfig.getConsumerTopic(), allTags);
	}

	public abstract RocketmqConfig getRocketmqConfig();

	public abstract MessageListenerConcurrently getMessageListener();

	public abstract String[] getAllListenerTags();

	private String combineTags(String[] tags) {
		if (tags == null) {
			throw new IllegalArgumentException("tags 不能为null");
		}
		if (tags.length == 1) {
			return tags[0];
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

	public void sendMessage(byte[] body, String tag) throws Exception {
		Message msg = new Message(rocketmqConfig.getPublishTopic(), tag, body);
		producer.sendOneway(msg);
	}

	public void shutdown() {
		if (this.producer != null) {
			this.producer.shutdown();
		}
		if (this.consumer != null) {
			this.consumer.shutdown();
		}
	}
}
