package com.xinyue.gateway.message;

import com.alibaba.fastjson.JSONObject;

public class GateLocalMessage {
	private GateMessageHeader header;
	private JSONObject body;

	public GateMessageHeader getHeader() {
		return header;
	}

	public void setHeader(GateMessageHeader header) {
		this.header = header;
	}

	public JSONObject getBody() {
		return body;
	}

	public void setBody(JSONObject body) {
		this.body = body;
	}

}
