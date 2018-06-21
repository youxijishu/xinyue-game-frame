package com.xinyue.rocketmq.framework.messagehandler;

import java.lang.reflect.Method;

import com.xinyue.network.message.common.IGameMessage;

public class MethodInvokerInfo {
	private Class<? extends IGameMessage> gameMessageClass;
	private Object object;
	private Method method;

	public MethodInvokerInfo(Object object, Method method,Class<? extends IGameMessage> gameMessageClazz) {
		super();
		this.object = object;
		this.method = method;
		this.gameMessageClass = gameMessageClazz;
	}

	public Class<? extends IGameMessage> getGameMessageClass() {
		return gameMessageClass;
	}

	public Object getObject() {
		return object;
	}

	public Method getMethod() {
		return method;
	}

}
