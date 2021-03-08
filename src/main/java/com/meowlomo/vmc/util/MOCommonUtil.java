package com.meowlomo.vmc.util;

import java.util.concurrent.TimeUnit;

public class MOCommonUtil {
	public static void sleepSeconds(long sec){
		try {
			TimeUnit.SECONDS.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void sleepMilliSeconds(long sec){
		try {
			TimeUnit.MILLISECONDS.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
