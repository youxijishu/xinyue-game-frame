package com.xinyue.rocketmq.framework.messagehandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;
/**
 * 此注解用于标记处理GameMessage的类和方法。
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年6月20日 下午10:04:31
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface MessageHandler {

}
