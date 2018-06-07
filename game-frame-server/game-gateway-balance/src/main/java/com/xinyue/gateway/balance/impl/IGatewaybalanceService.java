package com.xinyue.gateway.balance.impl;

import com.xinyue.gateway.balance.config.GatewayServerInfo;

public interface IGatewaybalanceService {
	/**
	 * 
	 * @Desc  根据用户id选择一个合适的网关连接信息。这里可以采用userid % 网关数量 来返回一个合适的网关。
	 * @param userId
	 * @return
	 * @Author 心悦网络  王广帅
	 * @Date 2018年6月6日 下午9:56:52
	 *
	 */
	GatewayServerInfo selectGateWay(long userId);
	
}
