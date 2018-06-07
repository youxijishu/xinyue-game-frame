package com.xinyue.user.error;

import com.xinyue.model.IServerError;

public enum UserServerError implements IServerError {
	USER_INFO_ERROR(4001, "注册信息有误"),;
	private int errorCode;
	private String errorMsg;

	private UserServerError(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String getErrorMsg() {
		return errorMsg;
	}

}
