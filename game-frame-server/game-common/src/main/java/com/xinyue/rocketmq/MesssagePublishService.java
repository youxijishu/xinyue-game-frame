package com.xinyue.rocketmq;

import java.util.List;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.xinyue.rocketmq.config.MessagePublishConfig;

import io.netty.buffer.ByteBuf;

public class MesssagePublishService {
	private DefaultMQProducer producer;
	private MessagePublishConfig publishConfig;

	public void start(MessagePublishConfig publishConfig) throws MQClientException {
		this.publishConfig = publishConfig;
		producer = new DefaultMQProducer(publishConfig.getGroupName());
		producer.setNamesrvAddr(publishConfig.getNameServerAddr());
		producer.setInstanceName(publishConfig.getInstanceName());
		producer.start();
	}

	public void sendMessage(long roleId, String tag, ByteBuf byteBuf)
			throws MQClientException, RemotingException, InterruptedException {
		byte[] body = byteBuf.array();
		String topic = publishConfig.getTopic();
		Message message = new Message(topic, tag, body);
		producer.sendOneway(message, new MessageQueueSelector() {

			@Override
			public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
				long id = (long) arg;
				int index = (int) (id % mqs.size());
				return mqs.get(index);
			}
		}, roleId);
	}

}
