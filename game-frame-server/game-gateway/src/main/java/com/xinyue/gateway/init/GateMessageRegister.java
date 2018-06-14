package com.xinyue.gateway.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.xinyue.network.message.common.GameMessageRegisterFactory;
import com.xinyue.network.message.common.IGameMessage;
/**
 * 这里注册网关直接处理的客户端请求命令。
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年6月14日 下午12:46:23
 */
@Service
public class GateMessageRegister {
	
	@PostConstruct
	public void init() {
		List<Class<? extends IGameMessage>> gameMessages = new ArrayList<>();
		GameMessageRegisterFactory.getInstance().registerCommandClass(gameMessages);
	}
}
