package com.xinyue.gateway.server.model;

import com.xinyue.network.message.common.IGameError;

public enum GateError implements IGameError {
	TOKEN_ERROR(401, "TOKEN错误"), TOKEN_EXPIRE(402, "token 过期"),;
	private int errorCode;
	private String errorMsg;

	private GateError(int errorCode, String errorMsg) {
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

	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder();
		msg.append("error:").append(this.errorCode).append("--").append(this.errorMsg);
		return msg.toString();
	}

}
