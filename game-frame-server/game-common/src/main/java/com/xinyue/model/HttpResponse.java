package com.xinyue.model;

public class HttpResponse {

	private Integer errorCode;
	private String errorMsg;
	private Object msg;

	public HttpResponse(IServerError error) {
		this.errorCode = error.getErrorCode();
		this.errorMsg = error.getErrorMsg();
	}

	public HttpResponse(Object msg) {
		super();
		this.msg = msg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public Object getMsg() {
		return msg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
