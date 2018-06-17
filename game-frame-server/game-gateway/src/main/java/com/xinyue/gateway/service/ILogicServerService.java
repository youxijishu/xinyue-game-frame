package com.xinyue.gateway.service;

import com.xinyue.network.EnumServerType;

/**
 * 此接品负责管理逻辑服务的配置和选择。同一种类型的服务可以横向扩展，即同一个服务可以同时启动多个实例。比如聊天服务，人多的时候可以启动多个聊天的服务。当收到一个客户端
 * 的消息时，网关需要给用户分配这个消息由哪个业务服务处理。按照同一个用户的数据始终路由到同一台服务上面。这里采用角色id和服务数量求余法计算。为了使可以动态更新服务的启动个数
 * 这里会对分配做一下缓存，这样当逻辑服务启动数量发生变化时，原来的用户还是去原来的服务器上面。
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年6月18日 上午12:48:35
 */
public interface ILogicServerService {

	short getToServerId(long roleId,EnumServerType serverType);
}
