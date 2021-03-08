package com.meowlomo.vmc.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.enterprise.module.bootstrap.Main;

public class RestartRebootUtils {
	private static final Logger logger = LoggerFactory.getLogger(RestartRebootUtils.class);

	public static void restart() {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		File currentJar;
		try {
			currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			/* is it a jar file? */
			if (!currentJar.getName().endsWith(".jar")) {
				logger.warn("Current program is not a jar file");
				return;
			} else {
				logger.info("Jar program [" + currentJar.getAbsolutePath() + "] restarting");
				/* Build command: java -jar application.jar */
				final ArrayList<String> command = new ArrayList<String>();
				command.add(javaBin);
				command.add("-jar");
				command.add(currentJar.getPath());

				final ProcessBuilder builder = new ProcessBuilder(command);
				builder.start();
				logger.info("Jar program [" + currentJar.getAbsolutePath() + "] restarted");
			}
		} catch (IOException e) {
			logger.error("Cought IOException in JarUtils::restart", e);
		} catch (URISyntaxException e) {
			logger.error("Cought URISyntaxException in JarUtils::restart", e);
		}
		System.exit(0);
	}

	public static void reboot() throws RuntimeException, IOException {
		String shutdownCommand;
		logger.info("Going to reboot the worker");
		if (PlatformUtil.linux()) {
			shutdownCommand = "reboot";
		} else if (PlatformUtil.mac()) {
			shutdownCommand = "shutdown -r now";
		} else if (PlatformUtil.windows()) {
			shutdownCommand = "shutdown.exe -r -f -t 0";
		} else {
			throw new RuntimeException("Unsupported operating system.");
		}
		Runtime.getRuntime().exec(shutdownCommand);
		System.exit(0);
	}
	
	public static boolean reboot(int time) throws IOException {
	    String shutdownCommand = null, t = time == 0 ? "now" : String.valueOf(time);

	    if(SystemUtils.IS_OS_AIX)
	        shutdownCommand = "shutdown -F -r " + t;
	    else if(SystemUtils.IS_OS_FREE_BSD
	            || SystemUtils.IS_OS_MAC
	            || SystemUtils.IS_OS_MAC_OSX
	            || SystemUtils.IS_OS_MAC
	            || SystemUtils.IS_OS_NET_BSD
	            || SystemUtils.IS_OS_OPEN_BSD
	            || SystemUtils.IS_OS_UNIX)
	        shutdownCommand = "shutdown -r " + t + "sec";
	    else if (SystemUtils.IS_OS_LINUX)
	        if (time < 60) {
	            t = "1";
	            shutdownCommand = "shutdown -r ";
	        }
	        else {
	            t = String.valueOf((time % 3600) / 60);
	            shutdownCommand = "shutdown -r " + t;
	        }
	    else if(SystemUtils.IS_OS_HP_UX)
	        shutdownCommand = "shutdown -ry " + t;
	    else if(SystemUtils.IS_OS_IRIX)
	        shutdownCommand = "shutdown -r" + t;
	    else if(SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS)
	        shutdownCommand = "shutdown -r" + t;
	    else if(SystemUtils.IS_OS_WINDOWS_XP
	            || SystemUtils.IS_OS_WINDOWS_VISTA
	            || SystemUtils.IS_OS_WINDOWS_7
	            || SystemUtils.IS_OS_WINDOWS_10
	            || SystemUtils.IS_OS_WINDOWS_8
	            || SystemUtils.IS_OS_WINDOWS)
	        shutdownCommand = "shutdown.exe -r -f -t " + t;
	    else
	        return false;
	    Runtime.getRuntime().exec(shutdownCommand);
	    return true;
	}
	
	public static boolean shutdown(int time) throws IOException {
	    String shutdownCommand = null, t = time == 0 ? "now" : String.valueOf(time);

	    if(SystemUtils.IS_OS_AIX)
	        shutdownCommand = "shutdown -Fh " + t;
	    else if(SystemUtils.IS_OS_FREE_BSD
	            || SystemUtils.IS_OS_LINUX
	            || SystemUtils.IS_OS_MAC
	            || SystemUtils.IS_OS_MAC_OSX
	            || SystemUtils.IS_OS_MAC
	            || SystemUtils.IS_OS_NET_BSD
	            || SystemUtils.IS_OS_OPEN_BSD
	            || SystemUtils.IS_OS_UNIX)
	        shutdownCommand = "shutdown -h " + t;
	    else if(SystemUtils.IS_OS_HP_UX)
	        shutdownCommand = "shutdown -hy " + t;
	    else if(SystemUtils.IS_OS_IRIX)
	        shutdownCommand = "shutdown -y -g " + t;
	    else if(SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS)
	        shutdownCommand = "shutdown -y -i5 -g" + t;
	    else if(SystemUtils.IS_OS_WINDOWS_XP
	            || SystemUtils.IS_OS_WINDOWS_VISTA
	            || SystemUtils.IS_OS_WINDOWS_7
	            || SystemUtils.IS_OS_WINDOWS_10
	            || SystemUtils.IS_OS_WINDOWS_8
	            || SystemUtils.IS_OS_WINDOWS)
	        shutdownCommand = "shutdown.exe -s -t " + t;
	    else
	        return false;
	    Runtime.getRuntime().exec(shutdownCommand);
	    return true;
	}
}
