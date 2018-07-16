package com.xinyue.gateway.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.xinyue.gateway.config.ServerConfig;
import com.xinyue.gateway.server.model.GateError;
import com.xinyue.gateway.server.model.GateMessageInfo;
import com.xinyue.gateway.server.model.GateUserInfo;
import com.xinyue.gateway.service.ChannelService;
import com.xinyue.network.message.impl.ConnectConfirmRequest;
import com.xinyue.network.message.impl.ConnectConfirmResponse;
import com.xinyue.utils.NettyUtil;
import com.xinyue.utils.TokenUtil;
import com.xinyue.utils.TokenUtil.TokenModel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * 这个handler负责和客户端建立连接，并且对连接进行验证。
 * 验证通过后，会缓存用户的id和角色的id.当接收到业务消息时，还会在这里给业务数据补充用户id和角色id.
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月14日 下午5:48:17
 */
@Service
@Scope(scopeName = "prototype")
public class GameGatewayConnectHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(GameGatewayConnectHandler.class);
	private static AtomicInteger channelCount = new AtomicInteger();
	private String clientIp;
	private ScheduledFuture<?> waitConnectConfirmFuture = null;
	private GateUserInfo gateUserInfo = null;
	@Autowired
	private ServerConfig serverConfig;
	@Autowired
	private ChannelService channelService;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.clientIp = NettyUtil.getIp(ctx);
		String channelId = ctx.channel().id().asShortText();
		logger.debug("新建连接{}，clientIp:{},当前连接数:[{}]", channelId, clientIp, channelCount.incrementAndGet());
		int timeout = serverConfig.getConnectTimeout();
		waitConnectConfirmFuture = ctx.executor().schedule(() -> {
			if (gateUserInfo == null) {
				logger.warn("客户端：{} 连接成功之后没有认证，关闭连接:{}", clientIp, channelId);
				ctx.close();
			}
		}, timeout, TimeUnit.SECONDS);

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("连接关闭，clientIp:{}", clientIp);
		if (gateUserInfo != null) {
			channelService.removeChannel(gateUserInfo.getRoleId(), ctx.channel().id());
		}
		channelCount.decrementAndGet();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 处理连接认证消息
		if (msg instanceof ConnectConfirmRequest) {
			ConnectConfirmRequest connectConfirmRequest = (ConnectConfirmRequest) msg;
			long roleId = connectConfirmRequest.getRoleId();
			long userId = connectConfirmRequest.getUserId();
			String token = connectConfirmRequest.getToken();
			GateError gateError = this.checkToken(connectConfirmRequest);
			ConnectConfirmResponse response = connectConfirmRequest.newResponse();
			if (gateError == null) {
				gateUserInfo = new GateUserInfo();
				gateUserInfo.setRoleId(roleId);
				gateUserInfo.setUserId(userId);
				gateUserInfo.setToken(token);
				channelService.addChannel(roleId, ctx.channel());
			    response.setResult(true);
				ctx.writeAndFlush(response);
				logger.debug("userId:{},roleId:{} token验证通过", userId, roleId);
			} else {
				response.setError(gateError);
				ctx.writeAndFlush(response);
				ctx.close();
				logger.warn("userId:{},roleId:{} token 验证失败,{}", userId, roleId, gateError);
			}
			if (waitConnectConfirmFuture != null) {
				waitConnectConfirmFuture.cancel(true);
			}
		} else {
			if (msg instanceof GateMessageInfo) {
				// 如果是业务消息，给这个消息添加上用户id和角色id的信息
				GateMessageInfo gateMessageInfo = (GateMessageInfo) msg;
				gateMessageInfo.setRoleId(this.gateUserInfo.getRoleId());
				gateMessageInfo.setUserId(this.gateUserInfo.getUserId());
				gateMessageInfo.setToken(this.gateUserInfo.getToken());
				long roleId = this.gateUserInfo.getRoleId();
				int total = gateMessageInfo.getMessageTotalSize();
				int messageId = gateMessageInfo.getMessageHead().getMessageId();
				logger.debug("==>roleId:{}, message size [{}],messageId:{}", roleId, total, messageId);
			}

			ctx.fireChannelRead(msg);
		}
	}

	/**
	 * 
	 * @Desc 检测token是否正确，是否过期
	 * @param connectConfirmRequest
	 * @return
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午10:24:49
	 *
	 */
	private GateError checkToken(ConnectConfirmRequest connectConfirmRequest) {
		
		long roleId = connectConfirmRequest.getRoleId();
		if(roleId > 0){
			return null;
		}
		long userId = connectConfirmRequest.getUserId();
		String token = connectConfirmRequest.getToken();
		TokenModel tokenModel = TokenUtil.getTokenModel(token);
		if (tokenModel == null) {
			logger.warn("token解析为空，有异常，说明token不对");
			return GateError.TOKEN_ERROR;
		}
		if (tokenModel.getUserId() == userId && tokenModel.getRoleId() == roleId) {
			long nowTime = System.currentTimeMillis();

			if (nowTime > tokenModel.getEndTime()) {
				logger.warn("roleId:{} - token 已过期", roleId);
				return GateError.TOKEN_EXPIRE;
			}
			return null;
		} else {
			logger.warn("token错误，用户id和角色id和token不匹配");
			return GateError.TOKEN_ERROR;
		}

	}

	public GateUserInfo getGateUserInfo() {
		return this.gateUserInfo;
	}
}
