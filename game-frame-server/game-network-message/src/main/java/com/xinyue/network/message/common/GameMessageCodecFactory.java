package com.xinyue.network.message.common;

/**
 * 负责消息的解码和编码及验证操作
 * @author 心悦网络科技有限公司   王广帅
 *
 * @Date 2018年6月10日 下午5:25:04
 */
import java.util.Map;

public class GameMessageCodecFactory {

	private int seqId = 0;
	private Map<Integer, Class<?>> messageClassMap = null;
	/**
	 * 
	 * @Desc  设置要解码的消息的id和对应的class
	 * @param commandClassMap
	 * @Author 心悦网络  王广帅
	 * @Date 2018年6月10日 下午5:31:03
	 *
	 */
	public void setCommandClass(Map<Integer, Class<?>> messageClassMap) {
		this.messageClassMap = messageClassMap;
	}
	

}
