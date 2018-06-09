package com.xinyue.web.user.service.model;

public class UserInfo {
	private String username;
	private String password;
	// 系统类型，1 安卓，2 ios,3,windows
	private int osType;
	// 第三方id，用于和第三使用推广产生的用户。
	private int thirdPartyId;

	private String registerIp;
	// 设备唯一号
	private String idfa;
	// 设备信息
	private String deviceMsg;

	private long registerTime;

	public int getThirdPartyId() {
		return thirdPartyId;
	}

	public void setThirdPartyId(int thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public int getOsType() {
		return osType;
	}

	public void setOsType(int osType) {
		this.osType = osType;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getDeviceMsg() {
		return deviceMsg;
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
