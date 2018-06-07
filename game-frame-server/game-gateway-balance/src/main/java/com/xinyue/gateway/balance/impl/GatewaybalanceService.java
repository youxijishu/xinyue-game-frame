package com.xinyue.gateway.balance.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.balance.config.GatewayServerConfig;
import com.xinyue.gateway.balance.config.GatewayServerInfo;

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

	@Override
	public GatewayServerInfo selectGateWay(long userId) {
		List<GatewayServerInfo> gatewayServerInfos = gatewayServerConfig.getGatewayServer();
		if (gatewayServerInfos != null && !gatewayServerInfos.isEmpty()) {
			int size = gatewayServerInfos.size();
			int index = (int) (userId % size);
			GatewayServerInfo selectResult = gatewayServerInfos.get(index);
			logger.debug("{}选择网关：{}", userId, selectResult);
			return selectResult;
		}
		return null;
	}

}
