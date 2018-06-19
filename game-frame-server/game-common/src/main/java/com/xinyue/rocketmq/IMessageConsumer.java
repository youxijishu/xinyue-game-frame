package com.xinyue.rocketmq;

import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;

public interface IMessageConsumer {

	void start(MessageListener listener,String tags) throws MQClientException;
	void shutdown();
}
