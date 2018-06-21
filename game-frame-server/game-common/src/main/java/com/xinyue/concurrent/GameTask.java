package com.xinyue.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GameTask implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(GameTask.class);
	@Override
	public void run(){
		try{
			this.doRun();
		}catch(Throwable e){
			logger.error("Game Task 异常",e);
		}
	}
	
	public abstract void doRun();
}
