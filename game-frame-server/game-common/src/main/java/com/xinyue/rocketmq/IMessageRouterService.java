package com.xinyue.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

public interface IMessageRouterService {

	void start() throws MQClientException;

	void shutdown();

	void sendMessage(byte[] buf, String tag) throws MQClientException, RemotingException, InterruptedException;

}
