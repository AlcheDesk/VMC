package com.meowlomo.vmc.task;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.osgi.OSGiUtil;

public class SGLogger {
//	private static String[] endStrings = new String[] { "INSTRUCTION_END", "FAIL", "ERROR", "TIME_OUT", "PASS", "ELEMENT_NOT_FOUND" };
//	private static String[] infoStrings = new String[] { "INSTRUCTION" };
	
	private static boolean standAlone = false;
	private static Object bundleHttpUtil = null;

	private static void formatLogMsg(String info, String message, String logLevel, String stepLogType) {
		String line = info + message;
		stepLogLogical(line, logLevel, stepLogType);
	}

	public static boolean setStandSingleton(boolean b) {
		return standAlone = b;
	}
	public static void setHttpBundleObj(Object o) {
		bundleHttpUtil = o;
	}

	public static Object updateRun(String info) {
		if (!standAlone && null != bundleHttpUtil)
			return OSGiUtil.doFunctionCall(bundleHttpUtil, "updateRun", info);
		return null;
	}

	public static void stepLogLogical(String line, String logLevel, String stepLogType) {
		ObjectNode info = JsonNodeFactory.instance.objectNode();
		info.put("line", line);
		info.put("logLevel", logLevel);
		info.put("stepLogType", stepLogType);
		System.err.println("stepLogLogical.out:" + info);
		
		if (!standAlone)
			OSGiUtil.doFunctionCall(bundleHttpUtil, "addLogicalStepLog", info.toString());
	}

	public static void info(String message) {
		formatLogMsg(" [      INFO      ] ", message, "INFO", "INSTRUCTION");
	}

	public static void warn(String message) {
		formatLogMsg(" [ WARN ] ", message, "WARN", "ERROR");
	}

	public static void instructionStart(String message) {
		formatLogMsg(" [    INS Begin    ] ", message, "INFO", "INSTRUCTION");
	}

	public static void instructionEnd(String message) {
		formatLogMsg(" [    INS End    ] ", message, "INFO", "PASS");
	}

	public static void errorMessage(String message) {
		formatLogMsg(" [ ERROR ] ", message, "ERROR", "FAIL");
	}

	public static void pass(String message) {
		formatLogMsg(" [      PASS      ] ", message, "INFO", "PASS");
	}
	
	 public static void faild(String message) {
		 formatLogMsg(" [      ERROR      ] ", message, "ERROR", "FAIL");
	 }
}
