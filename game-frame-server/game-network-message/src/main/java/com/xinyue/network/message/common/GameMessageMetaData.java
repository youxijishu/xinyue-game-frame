package com.xinyue.network.message.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xinyue.network.EnumServerType;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GameMessageMetaData {
	public EnumServerType serverType();
	public int id();
	public GameMessageType type();
}
