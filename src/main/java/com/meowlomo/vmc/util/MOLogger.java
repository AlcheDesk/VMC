package com.meowlomo.vmc.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class MOLogger {
	public static String error(Logger logger, String msg) {
		if (StringUtils.isNotEmpty(msg) && null != logger)
			logger.error(msg);
		return msg;
	}
	
	public static String info(Logger logger, String msg) {
		if (StringUtils.isNotEmpty(msg) && null != logger)
			logger.info(msg);
		return msg;
	}
	
	public static String errorDouble(Logger logger, String msg) {
		if (StringUtils.isNotEmpty(msg)) {
			if (null != logger)
				logger.error(msg);
			System.err.println(msg);
		}
		return msg;
	}
	
	public static String infoDouble(Logger logger, String msg) {
		if (StringUtils.isNotEmpty(msg)) {
			if (null != logger)
				logger.info(msg);
			System.out.println(msg);
		}
		return msg;
	}
}
