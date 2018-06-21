package com.xinyue.rocketmq.framework.messagehandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.IGameMessage;
import com.xinyue.network.message.common.MessageIdUtil;
import com.xinyue.rocketmq.framework.gamechannel.GameChannelHandlerContext;

/**
 * 这个类用于扫描标记了{@link MessageHandler}的类和方法。并从中提取要处理的GameMessage,并且和要处理的GameMessage的方法做映射。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月20日 下午10:06:35
 */
public class GameMessageMethodInvokerMapping {

	private Map<Integer, MethodInvokerInfo> methodMap = new HashMap<>();
	private List<Class<? extends IGameMessage>> allMessageClass = new ArrayList<>();
	private ApplicationContext applicationContext;

	public GameMessageMethodInvokerMapping(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;

	}

	public void scanGameMessageMapping() {
		// 获取所有添加了MessageHandler注解的所有bean对象的beanName
		String[] commandHandlerBeanNames = applicationContext.getBeanNamesForAnnotation(MessageHandler.class);
		if (commandHandlerBeanNames != null) {
			for (String beanName : commandHandlerBeanNames) {
				// 根据beanName获取对象
				Object obj = applicationContext.getBean(beanName);
				Method[] methods = obj.getClass().getMethods();
				for (Method method : methods) {
					MessageHandler messageHandler = method.getAnnotation(MessageHandler.class);
					if (messageHandler == null) {
						continue;
					}
					Class<?>[] parameterClazzes = method.getParameterTypes();
					if (parameterClazzes.length != 2) {
						throw new IllegalArgumentException("消息处理方法的参数不正确，参数必须是两个。");
					}

					Class<?> commandClass = parameterClazzes[0];
					if (!commandClass.isAssignableFrom(IGameMessage.class)) {
						throw new IllegalArgumentException("消息处理方法的参数不正确，第一个参数必须是IGameMessage或其子类");
					}
					if (!parameterClazzes[1].isAssignableFrom(GameChannelHandlerContext.class)) {
						throw new IllegalArgumentException("消息处理方法的参数不正确，第二个参数必须是GameChannelHandlerContext");
					}
					GameMessageMetaData messageMetaData = commandClass.getAnnotation(GameMessageMetaData.class);
					if (messageMetaData == null) {
						throw new IllegalArgumentException("消息处理方法的类型不对，没有GameMessageId注解,方法名："
								+ obj.getClass().getName() + "#" + method.getName());
					}
					int messageUniqueId = MessageIdUtil.getMessageUniqueId(messageMetaData.serverType(),
							messageMetaData.id());
					@SuppressWarnings("unchecked")
					Class<? extends IGameMessage> messageClass = (Class<? extends IGameMessage>) commandClass;
					allMessageClass.add(messageClass);
					MethodInvokerInfo methodInvokerInfo = new MethodInvokerInfo(obj, method, messageClass);
					this.methodMap.put(messageUniqueId, methodInvokerInfo);
				}
			}
			allMessageClass = Collections.unmodifiableList(allMessageClass);

		}
	}

	/**
	 * 
	 * @Desc 调用处理收到GameMessage请求的方法。
	 * @param gameMessage
	 * @param ctx
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月21日 下午3:34:04
	 *
	 */
	public void Invoker(IGameMessage gameMessage, GameChannelHandlerContext ctx) {
		Integer key = gameMessage.getMessageUniqueId();
		MethodInvokerInfo methodInvokerInfo = this.methodMap.get(key);
		if (methodInvokerInfo != null) {
			Object obj = methodInvokerInfo.getObject();
			try {
				methodInvokerInfo.getMethod().invoke(obj, gameMessage, ctx);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("404，找不到command:" + key + "的处理方法");
		}
	}

	public List<Class<? extends IGameMessage>> getAllGameMessageClass() {
		return allMessageClass;
	}

}
