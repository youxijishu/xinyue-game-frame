package com.xinyue.gateway.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.config.ServerConfig;
import com.xinyue.network.EnumServerType;

@Service
public class LogicServerService implements ILogicServerService {
	@Autowired
	private ServerConfig serverConfig;
	private Map<Long, List<ServerTypeAndServerId>> serverCache = new HashMap<>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 
	 * @Desc 从缓存中获取一个用户的一种服务类型对应的服务id
	 * @param roleId
	 * @param serverType
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月18日 上午1:20:48
	 *
	 */
	private Short getToServerIdFromCache(long roleId, EnumServerType serverType) {
		lock.readLock().lock();
		try {
			// 如果缓存中存在，直接使用缓存中的id
			List<ServerTypeAndServerId> serverTypeAndServerIds = serverCache.get(roleId);
			if (serverTypeAndServerIds != null) {
				for (ServerTypeAndServerId serverId : serverTypeAndServerIds) {
					if (serverId.getServerType() == serverType) {
						return serverId.getServerId();
					}
				}
			}
		} finally {
			lock.readLock().unlock();
		}
		return null;
	}

	private void addServerCache(long roleId, EnumServerType serverType, short serverId) {
		lock.writeLock().lock();
		try {
			List<ServerTypeAndServerId> serverIds = this.serverCache.get(roleId);
			if (serverIds == null) {
				serverIds = new ArrayList<>();
				this.serverCache.put(roleId, serverIds);
			}
			serverIds.add(new ServerTypeAndServerId(serverType, serverId));
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @Desc 计算一个到达的逻辑服务ServerId
	 * @param roleId
	 * @param serverType
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月18日 上午1:22:31
	 *
	 */
	private short calculateToServerId(long roleId, EnumServerType serverType) {
		// 计算serverId
		Map<Short, List<Short>> serverMap = serverConfig.getServerTypeMap();
		List<Short> serverList = serverMap.get(serverType.getServerType());
		if (serverList == null || serverList.isEmpty()) {
			throw new IllegalArgumentException("找不到serverType：" + serverType + "对应的服务列表");
		}
		int index = (int) (roleId % serverList.size());
		short serverId = serverList.get(index);
		return serverId;
	}

	@Override
	public short getToServerId(long roleId, EnumServerType serverType) {
		Short serverId = this.getToServerIdFromCache(roleId, serverType);
		if (serverId != null) {
			return serverId;
		}
		serverId = this.calculateToServerId(roleId, serverType);
		this.addServerCache(roleId, serverType, serverId);
		return serverId;
	}

	class ServerTypeAndServerId {
		private EnumServerType serverType;
		private short serverId;

		public ServerTypeAndServerId(EnumServerType serverType, short serverId) {
			super();
			this.serverType = serverType;
			this.serverId = serverId;
		}

		public EnumServerType getServerType() {
			return serverType;
		}

		public short getServerId() {
			return serverId;
		}

	}
}
