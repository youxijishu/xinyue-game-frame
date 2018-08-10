package com.xinyue.network;

import java.util.HashSet;
import java.util.Set;
/**
 * 服务类型的枚举
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年8月11日 上午1:27:06
 */
public enum EnumServerType {
	
	GAME_SERVER((short) 2001, "游戏服务"),;
	private short serverType;
	private String desc;

	private EnumServerType(short serverType, String desc) {
		this.serverType = serverType;
		this.desc = desc;
	}

	public short getServerType() {
		return serverType;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return serverType + ":" + desc;
	}
	public static EnumServerType getServerType(short serverType){
		for(EnumServerType type : EnumServerType.values()){
			if(type.getServerType() == serverType){
				return type;
			}
		}
		return null;
	}

	public static void checkServerType() {
		Set<Short> serverTypeSet = new HashSet<>();
		for (EnumServerType type : EnumServerType.values()) {
			if (serverTypeSet.contains(type.getServerType())) {
				throw new IllegalArgumentException("server type 不能有重复的，重复值：" + type);
			}
		}
	}

}
