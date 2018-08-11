package com.xinyue.gateway.server.codec;

import com.xinyue.utils.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class MessasgeBodyDecode {
    /**
     * 
     * @Desc  解码网关消息的包体，网关包体都是json串
     * @param buf
     * @param t
     * @return
     * @Author 心悦网络  王广帅
     * @Date 2018年8月11日 下午10:44:05
     *
     */
	public static <T> T  decode(ByteBuf buf,Class<T> t){
		if(buf.readableBytes() > 0){
			byte[] body = new byte[buf.readableBytes()];
			buf.readBytes(body);
			String json = new String(body,CharsetUtil.UTF_8);
			return JsonUtil.jsonToObj(json, t);
		}
		
		return null;
	}
}
