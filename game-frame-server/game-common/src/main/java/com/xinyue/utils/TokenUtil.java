package com.xinyue.utils;

import java.nio.ByteBuffer;

import org.springframework.util.Base64Utils;

public class TokenUtil {
	private static final String EncryptKey = "xinyue_token";

	/**
	 * 
	 * @Desc 创建token，格式：userId(8) + roleId (8) + 到期时间(8),然后采用aes对称加密
	 * @param userId
	 * @param roleId
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午4:42:36
	 *
	 */
	public static String createToken(TokenModel tokenModel) throws Exception {
		byte[] bytes = tokenModel.toBytes();
		byte[] result = AesUtil.encryptBuffer(EncryptKey, bytes);
		String token = Base64Utils.encodeToString(result);
		return token;
	}

	/**
	 * 
	 * @Desc 解密获取token信息
	 * @param token
	 * @return
	 * @throws Exception
	 * @Author 心悦网络 王广帅
	 * @Date 2018年6月14日 下午10:02:02
	 *
	 */
	public static TokenModel getTokenModel(String token) {
		byte[] bytes = Base64Utils.decodeFromString(token);
		try {
			bytes = AesUtil.decryptBuffer(EncryptKey, bytes);
		} catch (Exception e) {
			return null;
		}
		TokenModel tokenModel = new TokenModel();
		tokenModel.fromBytes(bytes);
		return tokenModel;
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			TokenModel tokenModel = new TokenModel(123, 124, 125);
			String token = TokenUtil.createToken(tokenModel);
			// System.out.println(token);
			tokenModel = TokenUtil.getTokenModel(token);
			// System.out.println(tokenModel);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	public static class TokenModel {
		private long userId;
		private long roleId;
		private long endTime;
		private static int capacity = 24;

		public TokenModel() {
		}

		public TokenModel(long userId, long roleId, long endTime) {
			super();
			this.userId = userId;
			this.roleId = roleId;
			this.endTime = endTime;
		}

		public byte[] toBytes() {
			ByteBuffer buf = ByteBuffer.allocate(capacity);
			buf.putLong(this.userId);
			buf.putLong(this.roleId);
			buf.putLong(this.endTime);
			buf.flip();
			return buf.array();
		}

		public void fromBytes(byte[] bytes) {
			if (bytes.length != capacity) {
				return;
			}
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			this.userId = buf.getLong();
			this.roleId = buf.getLong();
			this.endTime = buf.getLong();
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public long getRoleId() {
			return roleId;
		}

		public void setRoleId(long roleId) {
			this.roleId = roleId;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

		@Override
		public String toString() {
			return "TokenModel [userId=" + userId + ", roleId=" + roleId + ", endTime=" + endTime + "]";
		}

	}
}
