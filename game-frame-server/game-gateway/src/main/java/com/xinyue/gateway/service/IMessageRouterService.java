package com.xinyue.gateway.service;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.xinyue.network.message.inner.InnerMessageHeader;

public interface IMessageRouterService {

	void start() throws MQClientException;

	void shutdown();

	void sendMessage(byte[] buf, InnerMessageHeader header) throws MQClientException, RemotingException, InterruptedException;


}
