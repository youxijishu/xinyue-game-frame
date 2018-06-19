package com.xinyue.rocketmq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game.rocketmq")
public class RocketmqConfig {
	private String publishGroupName;
	//网关发布消息的topic
	private String publishTopic;
	//网关发布消息的topic key
	private String publishTopicKey;
	//网关发布消息的topic 队列数量
	private int publishTopicQueueNum;
	
	private String consumerGroupName;
	//网关消费队列的主题
	private String consumerTopic;
	private String instanceName = "No instance name";
	private String nameServerAddr;
	
	
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getPublishGroupName() {
		return publishGroupName;
	}
	public void setPublishGroupName(String publishGroupName) {
		this.publishGroupName = publishGroupName;
	}
	public String getConsumerGroupName() {
		return consumerGroupName;
	}
	public void setConsumerGroupName(String consumerGroupName) {
		this.consumerGroupName = consumerGroupName;
	}
	
	
	public String getPublishTopic() {
		return publishTopic;
	}
	public void setPublishTopic(String publishTopic) {
		this.publishTopic = publishTopic;
	}
	public String getPublishTopicKey() {
		return publishTopicKey;
	}
	public void setPublishTopicKey(String publishTopicKey) {
		this.publishTopicKey = publishTopicKey;
	}
	public int getPublishTopicQueueNum() {
		return publishTopicQueueNum;
	}
	public void setPublishTopicQueueNum(int publishTopicQueueNum) {
		this.publishTopicQueueNum = publishTopicQueueNum;
	}
	public String getConsumerTopic() {
		return consumerTopic;
	}
	public void setConsumerTopic(String consumerTopic) {
		this.consumerTopic = consumerTopic;
	}
	public String getNameServerAddr() {
		return nameServerAddr;
	}
	public void setNameServerAddr(String nameServerAddr) {
		this.nameServerAddr = nameServerAddr;
	}
	

	

}
