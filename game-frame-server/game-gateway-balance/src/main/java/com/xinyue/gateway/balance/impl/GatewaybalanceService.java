package com.xinyue.gateway.balance.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.balance.config.GatewayServerConfig;
import com.xinyue.gateway.balance.model.GatewayServerInfo;

/**
 * 负责管理网关的负载，从这里可以获取分配的网关id。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月6日 下午9:30:27
 */
@Service
public class GatewaybalanceService implements IGatewaybalanceService {
	private static final Logger logger = LoggerFactory.getLogger(GatewaybalanceService.class);
	@Autowired
	private GatewayServerConfig gatewayServerConfig;

	@PostConstruct
	public void init() {
		System.out.println("log level:" + logger.isDebugEnabled());
		System.out.println("log level:" + logger.isInfoEnabled());
		logger.debug("初始化成功：{}", gatewayServerConfig);
	}

	@Override
	public GatewayServerInfo selectGateWay(long userId) {

		return null;
	}

}
