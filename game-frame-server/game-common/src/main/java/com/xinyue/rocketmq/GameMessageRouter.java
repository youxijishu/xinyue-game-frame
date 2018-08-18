package com.xinyue.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.rocketmq.config.MessageConsumeConfig;
import com.xinyue.rocketmq.config.MessagePublishConfig;

import io.netty.buffer.ByteBuf;

public abstract class GameMessageRouter {
	private MesssagePublishService publishService;
	private MessageConsumeService consumeService;
	@Autowired
	private MessagePublishConfig publishConfig;
	@Autowired
	private MessageConsumeConfig consumeConfig;

	public void start() throws MQClientException {
		publishService = new MesssagePublishService();
		consumeService = new MessageConsumeService();
		publishService.start(publishConfig);
		consumeService.start(consumeConfig, this::receive);
	}

	public void sendMessage(long roleId, String tag, ByteBuf byteBuf)
			throws MQClientException, RemotingException, InterruptedException {
		publishService.sendMessage(roleId, tag, byteBuf);
	}

	public abstract void receive(ByteBuf byteBuf);
}
