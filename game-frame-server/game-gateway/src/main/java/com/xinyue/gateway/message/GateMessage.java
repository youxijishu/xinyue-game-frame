package com.xinyue.gateway.message;

import io.netty.buffer.ByteBuf;

public class GateMessage implements IGateMessage {

	private GateMessageHeader header;
	private ByteBuf body;

	@Override
	public GateMessageHeader getHeader() {

		return header;
	}

	@Override
	public ByteBuf getBody() {
		return body;
	}

	public void setHeader(GateMessageHeader header) {
		this.header = header;
	}

	public void setBody(ByteBuf body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "GateMessage [header=" + header + ",body size :"+ body.readableBytes() +"]";
	}

}
