package com.meowlomo.vmc.util;

public class PlatformUtil {
	private final static String operatingSystem = System.getProperty("os.name");
	public static boolean windows() {
		return operatingSystem.contains("Windows");
	}
	
	public static boolean linux() {
		return operatingSystem.contains("Linux");
	}
	
	public static boolean mac() {
		return operatingSystem.contains("Mac");
	}
}
