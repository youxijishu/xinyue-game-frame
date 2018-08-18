package com.xinyue.gateway.message;

import io.netty.buffer.ByteBuf;

public interface IGateMessage {
	
	GateMessageHeader getHeader();
    
	ByteBuf getBody();
}
