package com.xinyue.gateway.server.model;

import com.xinyue.network.message.common.IGameError;

public enum GateError implements IGameError {
	TOKEN_ERROR((short)401, "TOKEN错误"), 
	TOKEN_EXPIRE((short)402, "token 过期"),;
	private short errorCode;
	private String errorMsg;

	private GateError(short errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	@Override
	public short getErrorCode() {
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
