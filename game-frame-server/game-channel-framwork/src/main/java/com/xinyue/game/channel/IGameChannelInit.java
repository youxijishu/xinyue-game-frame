package com.xinyue.game.channel;

public interface IGameChannelInit {
	/**
	 * 
	 * @Desc 在GameChannel创建之后，会先调用此方法，进行channel相关的一些初始化。
	 * @param channel
	 * @Author 王广帅
	 * @Date 2018年5月15日 下午11:42:52
	 *
	 */
	void init(GameChannel channel);
}
