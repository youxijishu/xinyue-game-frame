package com.xinyue.eureka;

import com.xinyue.eureka.model.GameServerConfig;

public class AbstractGameServerService {

	public void setServerTypeAndServerId(String instanceId, GameServerConfig gameServerConfig) {

		if (instanceId == null) {
			return;
		}
		String[] instanceIdSplits = instanceId.split("-");
		if (instanceIdSplits.length != 2) {
			return;
		}
		int i = 0;
		int serverType = Integer.parseInt(instanceIdSplits[i++]);
		int serverId = Integer.parseInt(instanceIdSplits[i++]);
		gameServerConfig.setServerId(serverId);
		gameServerConfig.setServerType(serverType);
	}

}
