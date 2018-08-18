package com.xinyue.rocketmq.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game.message.consume")
public class MessageConsumeConfig {

	private String groupName;
	private String topic;
	private List<String> tags;
	private String nameServerAddr;
	private String instanceName;

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getNameServerAddr() {
		return nameServerAddr;
	}

	public void setNameServerAddr(String nameServerAddr) {
		this.nameServerAddr = nameServerAddr;
	}

}
