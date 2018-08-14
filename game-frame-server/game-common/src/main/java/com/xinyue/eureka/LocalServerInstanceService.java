package com.xinyue.eureka;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.stereotype.Service;

import com.xinyue.eureka.model.GameServerConfig;

/**
 * 管理本地服务的信息和配置
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年8月15日 上午12:05:50
 */
@Service
public class LocalServerInstanceService extends AbstractGameServerService {

	@Autowired
	private EurekaInstanceConfigBean instanceConfigBean;
	private GameServerConfig localServerInfo = new GameServerConfig();

	@PostConstruct
	public void init() {
		String instanceId = instanceConfigBean.getInstanceId();
		this.setServerTypeAndServerId(instanceId, localServerInfo);
	}

	public int getLocalServerId() {
		return localServerInfo.getServerId();
	}

}
