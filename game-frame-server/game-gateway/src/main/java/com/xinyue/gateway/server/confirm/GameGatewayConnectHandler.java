package com.xinyue.gateway.server.confirm;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xinyue.gateway.config.ServerConfig;
import com.xinyue.gateway.message.GateLocalMessage;
import com.xinyue.gateway.server.model.GateError;
import com.xinyue.gateway.server.model.GateUserInfo;
import com.xinyue.gateway.service.ChannelService;
import com.xinyue.gateway.utils.ChannelUtil;
import com.xinyue.utils.NettyUtil;
import com.xinyue.utils.TokenUtil;
import com.xinyue.utils.TokenUtil.TokenModel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * 这个handler负责和客户端建立连接，并且对连接进行验证。
 * 当服务器收到一个socket连接请求之后，会过一会检测用户是否验证通过，如果验证不通过，则关闭连接，以节省资源。
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
	// 连接认证的消息id
	private final static short ConfirmType = 1;

	private boolean isConfirmMessage(int type) {
		return type == ConfirmType;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.clientIp = NettyUtil.getIp(ctx);
		String channelId = ChannelUtil.getChannelId(ctx);
		logger.debug("新建连接,channelId {}，clientIp:{},当前连接数:[{}]", channelId, clientIp, channelCount.incrementAndGet());
		int timeout = serverConfig.getConnectTimeout();
		// 添加一个延迟定时器，过一会检测连接是否验证成功
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
		if (msg instanceof GateLocalMessage) {
			GateLocalMessage gateMessage = (GateLocalMessage) msg;
			if (gateMessage.getBody() == null) {
				logger.error("网关收到处理的消息包体为空");
				return;
			}
			JSONObject param = gateMessage.getBody();
			int type = param.getIntValue("type");
			if (this.isConfirmMessage(type)) {
				if (waitConnectConfirmFuture != null) {
					waitConnectConfirmFuture.cancel(true);
				}
				this.readGateUserInfo(param);
				GateError gateError = this.checkToken(gateUserInfo);
				if (gateError != null) {
					logger.warn("{} 验证没有通过，关闭连接,{}", clientIp, gateUserInfo);
					ctx.close();
				} else {
					channelService.addChannel(gateUserInfo.getRoleId(), ctx.channel());
					JSONObject result = new JSONObject();
					result.put("result", 1);
					result.put("type", ConfirmType);
					ctx.writeAndFlush(gateMessage);
				}
				logger.debug("channel [{}],ip [{}]  role [{}] 连接验证成功", ChannelUtil.getChannelId(ctx), clientIp,
						gateUserInfo.getRoleId());
			} else {
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}

	}

	private void readGateUserInfo(JSONObject param) {
		gateUserInfo = new GateUserInfo();
		gateUserInfo.setRoleId(param.getLongValue("roleId"));
		gateUserInfo.setUserId(param.getLongValue("userId"));
		gateUserInfo.setToken(param.getString("token"));
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
	private GateError checkToken(GateUserInfo userInfo) {

		TokenModel tokenModel = TokenUtil.getTokenModel(userInfo.getToken());
		if (tokenModel == null) {
			logger.warn("token解析为空，有异常，说明token不对");
			return GateError.TOKEN_ERROR;
		}
		long nowTime = System.currentTimeMillis();
		if (nowTime > tokenModel.getEndTime()) {
			logger.warn("roleId:{} - token 已过期", userInfo.getRoleId());
			return GateError.TOKEN_EXPIRE;
		}
		if (tokenModel.getUserId() == userInfo.getUserId() && tokenModel.getRoleId() == userInfo.getRoleId()) {

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
