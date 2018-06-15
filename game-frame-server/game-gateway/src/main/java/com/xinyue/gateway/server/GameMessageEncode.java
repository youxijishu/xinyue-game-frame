package com.xinyue.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
/**
 * 这里处理收到业务服务发送来的消息，加工成返回客户端的包。
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年6月14日 下午2:25:20
 */
public class GameMessageEncode extends ChannelOutboundHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		
		if(msg instanceof ByteBuf){
			
			promise.setSuccess();
		} else {
			ctx.write(msg, promise);
		}
	}
	/**
	 * 
	 * @Desc  从内包中获取消息的包头信息
	 * @param byteBuf
	 * @return
	 * @Author 心悦网络  王广帅
	 * @Date 2018年6月14日 下午3:02:42
	 *
	 */
//	private InnerMessageHeader getMessageHeader(ByteBuf byteBuf){
//		
//	}
}
