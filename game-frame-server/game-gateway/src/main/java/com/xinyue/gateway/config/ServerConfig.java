package com.xinyue.gateway.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game.server")
public class ServerConfig {
	private int serverId;
	private int port;
	private int bossThreads;
	private int workThreads;
	private long idleTime;
	private int connectTimeout;
	private Map<Short, List<Short>> serverTypeMap;
	
	

	

	public Map<Short, List<Short>> getServerTypeMap() {
		return serverTypeMap;
	}

	public void setServerTypeMap(Map<Short, List<Short>> serverTypeMap) {
		this.serverTypeMap = serverTypeMap;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public long getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBossThreads() {
		return bossThreads;
	}

	public void setBossThreads(int bossThreads) {
		this.bossThreads = bossThreads;
	}

	public int getWorkThreads() {
		return workThreads;
	}

	public void setWorkThreads(int workThreads) {
		this.workThreads = workThreads;
	}

	@Override
	public String toString() {
		return "ServerConfig [serverId=" + serverId + ", port=" + port + ", bossThreads=" + bossThreads
				+ ", workThreads=" + workThreads + ", idleTime=" + idleTime + ", connectTimeout=" + connectTimeout
				+ ", serverTypeMap=" + serverTypeMap + "]";
	}
	
}
