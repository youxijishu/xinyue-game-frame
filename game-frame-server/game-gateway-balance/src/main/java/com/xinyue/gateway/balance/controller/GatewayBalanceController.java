package com.xinyue.gateway.balance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xinyue.gateway.balance.config.GatewayServerInfo;
import com.xinyue.gateway.balance.impl.GatewaybalanceService;

@RestController
@RequestMapping("gateway")
public class GatewayBalanceController {
	@Autowired
	private GatewaybalanceService gatewaybalanceService;

	@RequestMapping("select/{userId}")
	public GatewayServerInfo selectGateway(@PathVariable("userId") long userId) {
		GatewayServerInfo gatewayServerInfo = gatewaybalanceService.selectGateWay(userId);
		return gatewayServerInfo;
	}
}
