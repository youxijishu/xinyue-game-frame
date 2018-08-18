package com.xinyue.rocketmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game.message.publish")
public class MessagePublishConfig {
	private String groupName;

	private String topic;

	private String topicKey;

	private int queueNum = 3;
	private String instanceName;
	private String nameServerAddr;

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getNameServerAddr() {
		return nameServerAddr;
	}

	public void setNameServerAddr(String nameServerAddr) {
		this.nameServerAddr = nameServerAddr;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTopicKey() {
		return topicKey;
	}

	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}

	public int getQueueNum() {
		return queueNum;
	}

	public void setQueueNum(int queueNum) {
		this.queueNum = queueNum;
	}

}
