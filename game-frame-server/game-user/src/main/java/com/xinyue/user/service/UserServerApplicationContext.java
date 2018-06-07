package com.xinyue.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 用户服务端的全局上下文管理类。负责管理全局变量的初始化
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月7日 下午10:59:03
 */
@Service
public class UserServerApplicationContext {
	private Logger logger = LoggerFactory.getLogger(UserRegisterService.class);
	@Value("${logic_threads}")
	private int logicThreads;
	private EventExecutorGroup executorGroup;
	private List<EventExecutor> eventExecutors;

	@PostConstruct
	public void init() {
		executorGroup = new DefaultEventExecutorGroup(logicThreads);
		eventExecutors = new ArrayList<>(logicThreads);
		executorGroup.forEach(executor -> {
			eventExecutors.add(executor);
		});
		logger.info("初始化逻辑线程数：{}", logicThreads);
	}

	/**
	 * 
	 * @Desc 按顺序获取下一个EventExecutor
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月7日 下午11:04:43
	 *
	 */
	public EventExecutor nextExecutor() {
		return executorGroup.next();
	}

	/**
	 * 
	 * @Desc 根据注册的用户名获取一个EventExecutor，同一个用户名返回的是同一个EventExecutor.
	 * @param userName
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月7日 下午11:05:06
	 *
	 */
	public EventExecutor getExecutorByUserName(String userName) {
		int hashCode = userName.hashCode();
		int index = hashCode % logicThreads;
		EventExecutor executor = eventExecutors.get(index);
		return executor;
	}
}
