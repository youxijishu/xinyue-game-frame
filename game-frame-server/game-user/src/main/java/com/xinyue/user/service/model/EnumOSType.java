package com.xinyue.user.service.model;

public enum EnumOSType {
	ANDROID(1, "安卓"), IOS(2, "ios"), WINDOWS(3, "windows"),;
	private int type;
	private String desc;

	private EnumOSType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public static boolean contains(int type){
		for(EnumOSType enumOSType : EnumOSType.values()){
			if(enumOSType.getType() == type){
				return true;
			}
		}
		return false;
	}
	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

}
