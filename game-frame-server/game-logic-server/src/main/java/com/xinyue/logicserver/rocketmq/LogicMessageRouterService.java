package com.xinyue.logicserver.rocketmq;

import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinyue.logicserver.config.ServerConfig;
import com.xinyue.rocketmq.IMessageConsumer;
import com.xinyue.rocketmq.IMessageProducer;
import com.xinyue.rocketmq.IMessageRouterService;

public class LogicMessageRouterService implements IMessageRouterService {
	@Autowired
	private IMessageProducer messageProducer;
	@Autowired
	private IMessageConsumer messageConsumer;
	@Autowired
	private ServerConfig serverConfig;

	@Override
	public void start() throws MQClientException {
		System.setProperty("rocketmq.client.log.loadconfig", "false");
		messageProducer.start();
		MessageListener listener = new LogicServerConsumerListener();
		//这里监听所有需要这个服务处理的消息的tags
		String tags = getAllTags();
		messageConsumer.start(listener, tags);
	}
	private String getAllTags(){
		
		return "";
	}

	@Override
	public void shutdown() {
		messageProducer.shutdown();
		messageConsumer.shutdown();
	}

	@Override
	public void sendMessage(byte[] buf, String tag) throws MQClientException, RemotingException, InterruptedException {
		messageProducer.producerMessage(buf, tag);
	}

}
