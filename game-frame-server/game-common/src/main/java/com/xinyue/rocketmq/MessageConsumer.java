package com.xinyue.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer implements IMessageConsumer {
	private DefaultMQPushConsumer consumer;
	@Autowired
	private RocketmqConfig rocketmqConfig;

	@Override
	public void start(MessageListener listener, String tags) throws MQClientException {
		consumer = new DefaultMQPushConsumer(rocketmqConfig.getConsumerGroupName());
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

		consumer.subscribe(rocketmqConfig.getConsumerTopic(), tags);
		if (listener instanceof MessageListenerConcurrently) {
			consumer.registerMessageListener((MessageListenerConcurrently) listener);
		} else if (listener instanceof MessageListenerOrderly) {
			consumer.registerMessageListener((MessageListenerOrderly) listener);
		}
		consumer.setInstanceName(rocketmqConfig.getInstanceName());
		consumer.start();
	}

	@Override
	public void shutdown() {
		if (consumer != null) {
			consumer.shutdown();
		}
	}

}
