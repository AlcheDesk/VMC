package com.meowlomo.vmc.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.file.FileServerCommunicator;
import com.meowlomo.vmc.osgi.OSGiUtil;

@Component
public class OSGiLoader {
	private static final Logger logger = LoggerFactory.getLogger(OSGiLoader.class);
	
	@Autowired
	FileServerCommunicator fileServerCommunicator;
	
	@Autowired
	RuntimeVariables vars;
	
	private void osgiLoadLog(String log){
		osgiLoadLog(log, "info");
	}
	
	private void osgiLoadLog(String log, String type){
		if (null == type || type.isEmpty())
			return;
		if (null == log || log.isEmpty())
			return;
		log = "		[OSGi configuration]" + log;
		switch(type.toLowerCase()){
		case "info":
			logger.info(log);
			break;
		case "error":
			logger.error(log);
			break;
		case "debug":
			logger.debug(log);
			break;
		case "warn":
			logger.warn(log);
			break;
		default:
			logger.info(log);
			break;
		}
	}
	
	private byte[] getIniContent(){
		String iniContent = "osgi.console=18888" + 
"osgi.bundles=reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.felix.gogo.runtime@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.felix.gogo.command@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.felix.gogo.shell@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.console@start,  reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.event@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.osgi.service.event@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.util@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.app@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.common@2:start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.ds@1:start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.preferences@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.equinox.registry@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\javax.xml@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.osgi.services@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.osgi.util@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.slf4j.api@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\ch.qos.logback.slf4j@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\ch.qos.logback.core@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\ch.qos.logback.classic@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\commons-collections@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\HikariCP@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\commons-configuration2@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.commons.jxpath@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.core.contenttype@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.core.jobs@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.core.contenttype@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.eclipse.core.runtime@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\json-20170516@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\mysql-connector-java-5.1.25@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.commons.codec@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.httpcomponents.httpclient@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.httpcomponents.httpcore@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\org.apache.commons.logging@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\commons-lang3@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\commons-beanutils@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\pluginsAoTian\\\\com.meowlomo.ci.ems.bundle.api@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\pluginsAoTian\\\\com.meowlomo.ci.ems.bundle.db@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\com.meowlomo.ci.ems.bundle.dbdriver@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\com.meowlomo.ci.ems.bundle.example@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\com.meowlomo.ci.ems.bundle.file@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\com.meowlomo.ci.ems.bundle.httpclient@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\com.meowlomo.ci.ems.bundle.logbackconf@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\com.meowlomo.ci.ems.bundle.logbackdbdriver@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\plugins\\\\com.meowlomo.ci.ems.bundle.usedb@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\pluginsAoTian\\\\com.meowlomo.ci.ems.bundle.webdriver.jar@start, reference\\:file\\:\\\\\\\\\\\\d:\\\\osgijar\\\\pluginsAoTian\\\\com.meowlomo.ci.ems.bundle.dbfragment.jar@start" +
"osgi.bundles.defaultStartLevel=4" + 
"osgi.configuration.cascaded=false " + 
"eclipse.ignoreApp=true";
		return iniContent.getBytes();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runOSGiFramework() {
		if (vars.closeSystemErr()){
			System.out.println("[Notice] set systemerr to empty");
			System.setErr(new PrintStream(new OutputStream() {
	            public void write(int b) {
	            }
	        }));
		}
		
		if (vars.closeSystemOut()){
			System.out.println("[Notice] set systemout to empty");
			System.setOut(new PrintStream(new OutputStream() {
	            public void write(int b) {
	            }
	        }));
		}
		
		//start osgi framework
		String logbackConfigFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\logbackDb.xml";

		logbackConfigFilePath = "";
		String frameworkConfig = "." + File.separator + "libs" + File.separator + "configuration";
		File osgiConfigurationFile = new File(frameworkConfig);
		if (!osgiConfigurationFile.exists()){
			frameworkConfig = reGenerateOSGiConfigFile(osgiConfigurationFile);
		}
		boolean osgiResult = OSGiUtil.initOSGi(logbackConfigFilePath, frameworkConfig);
		logger.info(String.format("OSGi framework %s.\n", osgiResult ? "started" : "starting failed, server will shutdown"));
		
		if (osgiResult){
			vars.setOSGiReady();
			
			//todo for example.
			//demonstrate how to use bundles.
			//1.首先,需要拿到服务的注册名的全名(由于可以进行扩展，所以未必能够直接通过A.class的形式获取，因为A都拿不到)
			//2.再通过名字获取到服务(对象)
			//3.调用服务的方法(doTest)拿到结果
			Object webDriverObj = OSGiUtil.getOSGiService("IWebDriver");
			Object dbObj = OSGiUtil.getOSGiService("IDataSource");
			
			//TODO 不再通过db bundle内置的dbconfig.xml来初始化，配合db driver的改造		
			logger.info("IWebDriver service get:" + webDriverObj);
			logger.info("IDataSource service get:" + dbObj);
			
			//如果版本不对，会退出
			OSGiUtil.exitIfOSGiIsNotReady(vars);
			
			vars.publisher.publishRegister();
		}else {
			System.exit(0);
		}
	} 

	/**
	 * 使用source中的config.ini来生成一个，如果没有，使用getIniContent中的内容生成一份配置
	 * @param osgiConfigurationFile
	 * @return
	 */
	private String reGenerateOSGiConfigFile(File osgiConfigurationFile) {
		osgiLoadLog(String.format("OSGi configuration file desired [%s] not founded, make the directory here. ", osgiConfigurationFile.getAbsolutePath()));
		String relativelyPath = System.getProperty("user.dir");
		String frameworkConfig = relativelyPath + "\\configuration";
		
		File newOSGiConfigFileDirectory = new File(frameworkConfig);
		File newOSGiConfigFile = new File(frameworkConfig + "\\config.ini");
		InputStream is = OSGiLoader.class.getClassLoader().getResourceAsStream("config.ini");
		osgiLoadLog(String.format("OSGi configuration file create here:[%s]", frameworkConfig + "\\config.ini"));
		if (!newOSGiConfigFileDirectory.exists()){
			newOSGiConfigFileDirectory.mkdirs();
			if (!newOSGiConfigFileDirectory.exists())
				osgiLoadLog(String.format("create new configuration directory failed:%s", frameworkConfig), "error");
			if (null != is)
				try {
					OutputStream ops = new FileOutputStream(newOSGiConfigFile);
					int len;
					byte[] buffer = new byte[1024];
					while((len = is.read(buffer)) != -1)
					{
						ops.write(buffer,0,len);
					}
					ops.close();
					
				} catch (IOException e) {
					osgiLoadLog(String.format("OSGi write configuration file using source ini file failed.Exception:%s", e), "error");
				}
			else {
				try {
					OutputStream ops = new FileOutputStream(newOSGiConfigFile);
					byte[] buffer = getIniContent();
					int len = buffer.length;
					ops.write(buffer,0,len);
					ops.close();
					
				} catch (IOException e) {
					osgiLoadLog(String.format("OSGi write configuration file using direct ini content failed.Exception:%s", e), "error");
				}
			}
		}
		return frameworkConfig;
	}
}
