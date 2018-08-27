package com.xinyue.web.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.DeferredResult;

import com.xinyue.concurrent.ReadAndWriteSet;
import com.xinyue.web.user.service.model.EnumOSType;
import com.xinyue.web.user.service.model.UserInfo;

/**
 * 负责用户的注册，为了防止用户名的重注册，所有的用户注册请求都会放到同一个线程中执行。
 * 
 * @author 心悦网络科技有限公司 王广帅
 *
 * @Date 2018年6月7日 下午10:38:42
 */
@Service
public class UserRegisterService {
	private Logger logger = LoggerFactory.getLogger(UserRegisterService.class);
	@Autowired
	private UserServerApplicationContext context;
	private ReadAndWriteSet<String> registerUserNameSet = new ReadAndWriteSet<>();
	// @Value("${user-name-min-length}")
	private int userNameMinLength;
	// @Value("${user-name-max-length}")
	private int userNameMaxLength;

	public void register(UserInfo userInfo, DeferredResult<Object> result) {

	}

	private boolean checkUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			logger.error("注册信息不能为空");
			return false;
		}
		String username = userInfo.getUsername();
		if (StringUtils.isEmpty(username)) {
			logger.error("用户名不能为空");
			return false;
		}
		if (username.length() < userNameMinLength || username.length() > userNameMaxLength) {
			logger.error("用户名长度不合法，范围：[{},{}],当前长度：{}", userNameMinLength, userNameMaxLength, username.length());
			return false;
		}
		if (StringUtils.isEmpty(userInfo.getPassword())) {
			logger.error("密码不能为空");
			return false;
		}
		String password = userInfo.getPassword();
		if (password.length() < userNameMinLength || password.length() > userNameMaxLength) {
			logger.error("用户名长度不合法，范围：[{},{}],当前长度：{}", userNameMinLength, userNameMaxLength, password.length());
			return false;
		}

		if (!EnumOSType.contains(userInfo.getOsType())) {
			logger.error("操作系统类型不存在:{}", userInfo.getOsType());
			return false;
		}
		String idfa = userInfo.getIdfa();
		if (!StringUtils.isEmpty(idfa)) {
			if (idfa.length() > 32) {
				logger.error("idfa太长了，最大32，当前:{}", idfa.length());
				return false;
			}
		}
		String deviceMsg = userInfo.getDeviceMsg();
		if (!StringUtils.isEmpty(deviceMsg)) {
			if (deviceMsg.length() > 100) {
				logger.error("device msg 太长了，最多100，当前：{}", deviceMsg.length());
				return false;
			}
		}
		return true;
	}

}
