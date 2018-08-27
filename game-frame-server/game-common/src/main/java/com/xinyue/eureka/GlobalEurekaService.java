package com.xinyue.eureka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.xinyue.eureka.model.GameServerConfig;

@Service
public class GlobalEurekaService extends AbstractGameServerService implements ApplicationListener<HeartbeatEvent> {

	@Autowired
	private EurekaClient eurekaClient;

	private Map<Integer, List<Integer>> logicServerMap = new ConcurrentHashMap<>();
	private static Logger logger = LoggerFactory.getLogger(GlobalEurekaService.class);

	@PostConstruct
	public void init() {

		eurekaClient.registerEventListener(new EurekaEventListener() {

			@Override
			public void onEvent(EurekaEvent event) {
				System.out.println("收到事件：" + event.getClass().getName());
			}
		});

		this.refreshLogicServerInstance();

	}

	public void refreshLogicServerInstance() {
		Applications applications = eurekaClient.getApplications();
		List<Application> applications2 = applications.getRegisteredApplications();
		Map<Integer, List<Integer>> logicServerMap = new ConcurrentHashMap<>();
		for (Application application : applications2) {
			List<InstanceInfo> instances = application.getInstances();
			for (InstanceInfo info : instances) {
				this.setServerTypeAndServerId(info, logicServerMap);
			}
		}
		this.logicServerMap = logicServerMap;
	}

	private void setServerTypeAndServerId(InstanceInfo info, Map<Integer, List<Integer>> resultMap) {
		GameServerConfig gameServerConfig = new GameServerConfig();
		String instanceId = info.getInstanceId();
		this.setServerTypeAndServerId(instanceId, gameServerConfig);
		int serverId = gameServerConfig.getServerId();
		int serverType = gameServerConfig.getServerType();
		List<Integer> serverIds = resultMap.get(serverType);
		if (serverIds == null) {
			serverIds = new ArrayList<>();
			resultMap.put(serverType, serverIds);
		}
		serverIds.add(serverId);
		logger.debug("注册的实例信息：appName:{},instanceId:{}", info.getAppName(), info.getInstanceId());
	}

	public int selectServerId(long roleId, int serverType) {
		List<Integer> serverIds = this.logicServerMap.get(serverType);
		if (serverIds == null) {
			return -1;
		}
		int index = (int) (roleId % serverIds.size());
		int serverId = serverIds.get(index);
		return serverId;
	}

	@Override
	public void onApplicationEvent(HeartbeatEvent event) {

		this.refreshLogicServerInstance();
	}

}
