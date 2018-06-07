package com.xinyue.gateway.balance.config;

public class GatewayServerInfo {
	private int serverId;
	private int port;
	private String host;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	@Override
	public String toString() {
		return "GatewayServer [serverId=" + serverId + ", port=" + port + ", host=" + host + "]";
	}

}
