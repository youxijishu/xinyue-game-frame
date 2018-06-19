package com.xinyue.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer implements IMessageProducer {
	private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);
	private DefaultMQProducer producer;
	@Autowired
	private RocketmqConfig gateRocketmqConfig;

	private void init() throws MQClientException {
		
		producer.createTopic(gateRocketmqConfig.getPublishTopicKey(), gateRocketmqConfig.getPublishTopic(),
				gateRocketmqConfig.getPublishTopicQueueNum());
	}

	@Override
	public void start() throws MQClientException {
		producer = new DefaultMQProducer(gateRocketmqConfig.getPublishGroupName());
		producer.setNamesrvAddr(gateRocketmqConfig.getNameServerAddr());
		logger.info("RocketMQ nameServer address:{}", gateRocketmqConfig.getNameServerAddr());
		producer.start();
		
		String str = "aaa";
		try {
			this.producerMessage(str.getBytes(), "str");
		} catch (RemotingException | InterruptedException e) {
			e.printStackTrace();
		}
		//this.init();
	}

	@Override
	public void shutdown() {
		if (producer != null) {
			producer.shutdown();
		}
	}

	@Override
	public void producerMessage(byte[] buf, String tag)
			throws MQClientException, RemotingException, InterruptedException {
		Message msg = new Message(gateRocketmqConfig.getPublishTopic(), tag, buf);
		if (producer == null) {
			throw new NullPointerException("producer 未设置");
		}
		producer.sendOneway(msg);
	}

}
