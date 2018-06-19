package com.xinyue.gateway.service;

import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.config.ServerConfig;
import com.xinyue.model.GameCommonConstants;
import com.xinyue.network.message.inner.InnerMessageHeader;
import com.xinyue.rocketmq.IMessageConsumer;
import com.xinyue.rocketmq.IMessageProducer;

@Service
public class MessageRouterService implements IMessageRouterService{
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
		MessageListener listener = new GateMessageSubscibeListener();
		//监听所有带这个tags的消息。在网关向逻辑服务发送消息的时候，在包头会带有fromServerId的字段，这个fromServerId就是网关的serverId。
		//逻辑服务在处理完业务要返回给网关消息时，它产生的消息都会带上这个tags。这里进行监听。
		String tags = String.valueOf(serverConfig.getServerId());
		messageConsumer.start(listener, tags);
	}

	@Override
	public void shutdown() {
		messageProducer.shutdown();
		messageConsumer.shutdown();
	}

	@Override
	public void sendMessage(byte[] buf, InnerMessageHeader header) throws MQClientException, RemotingException, InterruptedException {
		String tag = this.getMessageTag(header);
		messageProducer.producerMessage(buf, tag);
	}
	private String getMessageTag(InnerMessageHeader header){
		StringBuilder tagBuild = new StringBuilder();
		tagBuild.append(GameCommonConstants.MessageTagPrefix).append(header.getServerType().getServerType()).append(":").append(header.getMessageId()).append(":").append(header.getToServerId());
		return tagBuild.toString();
	}
}
