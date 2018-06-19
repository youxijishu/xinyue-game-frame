package com.xinyue.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

public interface IMessageProducer {
	public void start() throws MQClientException;
	public void shutdown();
	void producerMessage(byte[] buf, String tag) throws MQClientException, RemotingException, InterruptedException;
}
