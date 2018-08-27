package com.xinyue.web.gateway.balance.config;

import java.util.List;

public class GatewayServerConfig {

	private List<GatewayServerInfo> gatewayServer;

	public List<GatewayServerInfo> getGatewayServer() {
		return gatewayServer;
	}

	public void setGatewayServer(List<GatewayServerInfo> gatewayServer) {
		this.gatewayServer = gatewayServer;
	}

	@Override
	public String toString() {
		return "GatewayServerConfig [gatewayServer=" + gatewayServer + "]";
	}

}
