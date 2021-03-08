/**
 * 
 */
package com.meowlomo.vmc.osgi;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.meowlomo.vmc.config.RuntimeVariables;

/**
 * @author 陈琪
 * 
 *
 */
public class OSGiUtil {
	
	private static class BundleVersionInfo{
		Version requiredVersion;
		Version actualVersion;
		
		public BundleVersionInfo(Version require, Version actual) {
			requiredVersion = require;
			actualVersion = actual;
		}
	}
	
	public static String MOInterfacePackageName = "com.meowlomo.ci.ems.bundle.interfaces.";
	private static final Logger logger = LoggerFactory.getLogger(OSGiUtil.class);
	private static OSGiFramework _pluginFramework;
	
	final static int UNINSTALLED = 1;
	final static int INSTALLED = 2;
	final static int RESOLVED = 4;
	final static int STARTING = 8;
	final static int STOPPING = 16;
	final static int ACTIVE = 32;
	
	private static BundleContext _context;
	private static boolean inited = false;
	
	static{
		_pluginFramework = new DefaultOSGiFramework();
	}
	
	public static synchronized boolean inited(){
		return inited;
	}
	
	public static Bundle getBundle(String bundleName) {
		//TODO
		return _context.getBundle(bundleName);
	}
	
	//TODO 提前预传全局数据
	public static Boolean setupExecute(Object bundleImplObj, String massState) {
		// TODO
		Object result = doFunctionCall(bundleImplObj, "attachData", massState);
		if (result instanceof Boolean) {
			return (Boolean) result;
		} else if (null == result) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean teardownExecute(Object bundleImplObj) {
		//TODO
		Object result = doFunctionCall(bundleImplObj, "clearState");
		if (result instanceof Boolean) {
			return (Boolean) result;
		} else if (null == result) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param host the reference of the host app.
	 * @return true if the OSGi framework is ready to work(which means initial job is OK).
	 */
	private static synchronized boolean initOSGi(String frameworkConfig){
		BundleContext bc = (null != _pluginFramework ? _pluginFramework.initFramework(frameworkConfig) : null);
		if (null != bc){
			_context = bc;
		}
		return inited = null != bc;
	}
	
	public static synchronized boolean initOSGi(String logbackConfig, String frameworkConfig){
		
		logger.info(String.format("	OSGi inited by config:	%s", frameworkConfig));
		if (!logbackConfig.isEmpty()){
			File tmpFile = new File(logbackConfig);
			if (tmpFile.isFile() && tmpFile.getName().toLowerCase().endsWith(".xml")){
				//todo,没有更好的方案,bundle会去找三个特定路径特定名字的配置，通过系统属性传导是目前最不具有侵入性的方式
				System.setProperty("logback.configurationFile", logbackConfig);
			}
		}
		return inited = initOSGi(frameworkConfig);
	}
	
	public static synchronized void exitOSGi(){
		if (null != _pluginFramework){
			_pluginFramework.closeFramework();
			_pluginFramework = null;
		}
		inited = false;
	}
	
	public static boolean addOSGiBundle(String bundleSrc){
		Bundle bd = _pluginFramework.addBundle(bundleSrc);
		return null != bd;
	}
	
	public static boolean removeOSGiBundle(String bundleName){
		return _pluginFramework.removeBundle(bundleName);
	}
	
	public static boolean updateOSGiBundle(String bundleName, String bundleSrc){
		Bundle bd = _pluginFramework.updateBundle(bundleName, bundleSrc);
		return null != bd;
	}
	
	public static boolean stopOSGiBundle(String bundleName){
		return _pluginFramework.stopBundle(bundleName);
	}
	
	public static String doTestWork(String bundleName, String jsonParam){
		return _pluginFramework.doTest(bundleName, jsonParam);
	}
	
	public static Object doFunctionCall(Object serviceObj, String funcName){
		Object result = null;
		try{
			Method[] methods = serviceObj.getClass().getMethods();
			logger.info("Methods for {}: {}"
					, serviceObj.getClass().getName()
					, String.join(",", Lists.transform(Arrays.asList(methods), new Function<Method, String>(){
				public String apply(Method method) {
					return method.getName();
				}
			})));

			for (Method method : methods) {
				if (method.getName().equalsIgnoreCase(funcName)) {
					Class<?>[] paramTypes = method.getParameterTypes();
					if (0 == paramTypes.length) {
						try {
							result = method.invoke(serviceObj);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 
	 */
	public static Object doFunctionCall(Object serviceObj, String funcName, String jsonParam){
		Object result = null;
		boolean bExecuted = false;
		Method[] methods = serviceObj.getClass().getMethods();
		logger.info("Methods for {}: {}"
				, serviceObj.getClass().getName()
				, String.join(",", Lists.transform(Arrays.asList(methods), new Function<Method, String>(){
			public String apply(Method method) {
				return method.getName();
			}
		})));
		
		for(Method method : methods){
			if (method.getName().equalsIgnoreCase(funcName)){
				Class<?>[] paramTypes = method.getParameterTypes();
				if (1 == paramTypes.length){
					Class<?> paramType = paramTypes[0];
					if (paramType == String.class){
						try {
							bExecuted = true;
							logger.info(String.format("Begin to invoke method using doFunctionCall : %s,with service object:%s", method.getName(), serviceObj.toString()));
							result = method.invoke(serviceObj, jsonParam);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							result = null;
						}
						break;
					}
				}
			}
		}
		if (!bExecuted)
			logger.info(String.format("No execution for object - %s : method - %s", serviceObj.getClass().toString(), funcName));
		return result;
	}
	
	public static Object doFunctionCall(Object serviceObj, String funcName, String jsonParam, List<String> params){
		Object result = null;
		boolean bExecuted = false;

		Method[] methods = serviceObj.getClass().getMethods();
		for(Method method : methods){
			logger.info(method.getName());
			if (method.getName().equalsIgnoreCase(funcName)){
				Class<?>[] paramTypes = method.getParameterTypes();
				if (2 == paramTypes.length){
					Class<?> paramType1 = paramTypes[0];
					Class<?> paramType2 = paramTypes[1];
					if (paramType1 == String.class && paramType2 == List.class){
						try {
							bExecuted = true;
//							logger.info(String.format("Begin to invoke method using doFunctionCall : %s,with service object:%s", method.getName(), serviceObj.toString()));
							result = method.invoke(serviceObj, jsonParam, params);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							result = null;
						}
						break;
					}
				}
			}
		}
		if (!bExecuted)
			logger.info(String.format("No execution for object - %s : method - %s", serviceObj.getClass().toString(), funcName));
		return result;
	}
	
	public static String getStateName(int state) {
		switch(state){
			case UNINSTALLED:
				return "UNINSTALLED"; 
			case INSTALLED:
				return "INSTALLED";
			case RESOLVED:
				return "RESOLVED";
			case STARTING:
				return "STARTING";
			case STOPPING:
				return "STOPPING";
			case ACTIVE:
				return "ACTIVE";
			default:
				return "undefined";
		}
	}
	
//	public static void innerAddDataSource(String dbUrl, String dbUser, String dbPwd, String dbClassName, String dbName){
//		Object dbObj = OSGiUtil.getOSGiService("IDataSource");
//		if (null != dbObj){
//			try {
//				Method innerAddFunc = dbObj.getClass().getMethod("innerAddDataSource", String.class, String.class,
//						String.class, String.class, String.class);
//				innerAddFunc.invoke(dbObj, dbUrl, dbUser, dbPwd, dbClassName, dbName);
//			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
//	public static void addDataSource(String dbUrl, String dbUser, String dbPwd, String dbClassName, String dbName){
//		Object dbObj = OSGiUtil.getOSGiService("IDataSource");
//		if (null != dbObj){
//			try {
//				Method addFunc = dbObj.getClass().getMethod("addDataSource", String.class, String.class,
//						String.class, String.class, String.class);
//				addFunc.invoke(dbObj, dbUrl, dbUser, dbPwd, dbClassName, dbName);
//			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public static void setupZooKeeper(String host, String port) {
//		//TODO to be delete, for called by bundles attachData() batch.
//	}
//	
//	public static void clearZooKeeper() {
//		//TODO to be delete, for called by bundles clearState() batch.
//	}
	
//	public static void addRedis(String redisName, String redisHost, int port, boolean cluster) {
//		Object redisObj = OSGiUtil.getOSGiService("IRedis");
//		if (null != redisObj) {
//			try {
//				Method addRedis = redisObj.getClass().getMethod("addRedis", String.class, String.class, int.class, boolean.class);
//				addRedis.invoke(redisObj, redisName, redisHost, port, cluster);
//			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
//					| InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void addRedis(String redisName, String redisHost, int port, String password, boolean cluster) {
//		Object redisObj = OSGiUtil.getOSGiService("IRedis");
//		if (null != redisObj) {
//			try {
//				Method addRedis = redisObj.getClass().getMethod("addRedis", String.class, String.class, int.class,
//						String.class, boolean.class);
//				addRedis.invoke(redisObj, redisName, redisHost, port, password, cluster);
//			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
//					| InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//   public static void addRedis(
//           Boolean pool, 
//           String redisName, 
//           String host,  
//           Boolean ssl,   
//           String pwd, 
//           Integer port, 
//           Integer timeout, 
//           Integer connectionTimeout, 
//           Integer soTimeout, 
//           Integer maxTotal, 
//           Integer maxIdle, 
//           Integer minIdle){
//        Object dbObj = OSGiUtil.getOSGiService("IRedis");
//        if (null != dbObj){
//            try {
//                /*void innerAddRedis(
//                Boolean pool, 
//                String redisName, 
//                String host,  
//                Boolean ssl,   
//                String pwd, 
//                Integer port, 
//                Integer timeout, 
//                Integer connectionTimeout, 
//                Integer soTimeout, 
//                Integer maxTotal, 
//                Integer maxIdle, 
//                Integer minIdle); */
//                Method addFunc = dbObj.getClass().getMethod("addRedis", 
//                        Boolean.class, 
//                        String.class, 
//                        String.class, 
//                        Boolean.class, 
//                        String.class, 
//                        Integer.class, 
//                        Integer.class, 
//                        Integer.class, 
//                        Integer.class, 
//                        Integer.class, 
//                        Integer.class, 
//                        Integer.class);
//                
//                addFunc.invoke(dbObj,
//                        pool, 
//                        redisName, 
//                        host,  
//                        ssl,   
//                        pwd, 
//                        port, 
//                        timeout, 
//                        connectionTimeout, 
//                        soTimeout, 
//                        maxTotal, 
//                        maxIdle, 
//                        minIdle);
//            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//	
//	public static boolean removeRedis(String redisName) {
//		Object redisObj = OSGiUtil.getOSGiService("IRedis");
//		boolean reoveResult = true;
//		if (null != redisObj){
//			try {
//				Method removeFunc = redisObj.getClass().getMethod("removeRedis", String.class);
//				removeFunc.invoke(redisObj, redisName);
//			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//				reoveResult = false;
//			}
//		}
//		return reoveResult;
//	}
//		
//	public static boolean removeDataSource(String dsName){
//		Object dbObj = OSGiUtil.getOSGiService("IDataSource");
//		boolean reoveResult = true;
//		if (null != dbObj){
//			try {
//				Method removeFunc = dbObj.getClass().getMethod("removeDataSource", String.class);
//				removeFunc.invoke(dbObj, dsName);
//			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//				reoveResult = false;
//			}
//		}
//		return reoveResult;
//	}
	
	public static Object getOSGiService(String serviceName) {
		if (!serviceName.contains(".")) {
			serviceName = MOInterfacePackageName + serviceName;			
		}
		
        ServiceReference<?> serviceRef = _context.getServiceReference(serviceName);  
        return getOSGiServiceByServicRef(serviceRef);
    }
	
	public static <T> T getOSGiService(Class<T> serviceClass) {  
        ServiceReference<T> serviceRef = _context.getServiceReference(serviceClass);  
        return getOSGiServiceByServicRef(serviceRef);
    }
	
	private static <T> T getOSGiServiceByServicRef(ServiceReference<T> serviceRef){
		if (null == serviceRef)  
            return null;  
        return _context.getService(serviceRef);
	}
	
	/** 
     * 获取OSGi容器中插件的类 
     */  
    public static Class<?> getBundleClass(String bundleName, String className)
            throws Exception {  
        Bundle[] bundles = _context.getBundles();  
        for (int i = 0; i < bundles.length; i++) {  
            if (bundleName.equalsIgnoreCase(bundles[i].getSymbolicName())) {  
                return bundles[i].loadClass(className);  
            }  
        }  
        return null;  
    }
    
    public static void exitIfOSGiIsNotReady(RuntimeVariables vars) {
    	String errInfo = "";
    	Map<String, BundleVersionInfo> bundleVersionNotMatch = new HashMap<String, BundleVersionInfo>(10);
    	Set<String> mlBundleNames = null;
    	if (null == _context) {
    		errInfo = "OSGi Environment is not ready.";
    	}
    	else{
    		//1.获取bundle
    		Bundle[] bundles = _context.getBundles();
    		if (null == bundles)
    			errInfo = "OSGi bundles is not ready.";
    		else{
    			Map<String, String> bundleInfo = vars.bundleMap.getVersion();
    			mlBundleNames = bundleInfo.keySet();
    			for(Bundle bundle : bundles) {
    				String bdSymbolicName = bundle.getSymbolicName();
    				//TODO 
    				if (bdSymbolicName.contains("com.meowlomo.")) {
    					String bdName = bdSymbolicName.substring(bdSymbolicName.lastIndexOf(".") + 1);
	    				if (!bdName.isEmpty() && bundleInfo.containsKey(bdName)) {
	    					Version actualVer = bundle.getVersion();
	    					Version requiredVer = new Version(bundleInfo.get(bdName));
	    					mlBundleNames.remove(bdName);
	    					//2.针对SymbolicName比较version
	    					if (requiredVer.compareTo(actualVer) <= 0) {
	    						logger.info(String.format("bundle %s version : [%s] satisfied! required :[%s]", bdSymbolicName, actualVer, requiredVer));
	    					} else {
	    						logger.info(String.format("bundle %s version : [%s] not satisfied! required :[%s]", bdSymbolicName, actualVer, requiredVer));
	    						bundleVersionNotMatch.put(bdSymbolicName, new BundleVersionInfo(requiredVer, actualVer));
	    					}
	    				}
	    			}
    			}
    			
    		}
    	}
		
		//3.第2条不匹配的不通过.
    	if (!bundleVersionNotMatch.isEmpty()) {
    		for(Map.Entry<String, BundleVersionInfo> entry : bundleVersionNotMatch.entrySet()) {
    			String infoString = String.format("bundle %s version : %s dosn't satisfied the requried version : %s\n", entry.getKey(), entry.getValue().actualVersion, entry.getValue().requiredVersion);
    			errInfo += infoString + "\n\t";
    		}
    	}
    	
    	//4.要强制匹配的没有匹配到
    	if (null != mlBundleNames && mlBundleNames.size() > 0) {
    		errInfo += "bundles :" + String.join(",", mlBundleNames) + " 不满足";
    	}
    	
    	if (!errInfo.isEmpty()) {
    		errInfo += "\n\tVMC will exit !!!";
    		logger.error("\n" + errInfo);
    		System.err.println(errInfo);
    		System.exit(0);
    	}
    }
    
    public static Object executeReloadRequest(ObjectNode requestJson){
    	if (requestJson.has("bundleName") && requestJson.has("bundlePath")){
	    	String name = requestJson.get("bundleName").asText();
	    	String path = requestJson.get("bundlePath").asText();
	    	Boolean showName = requestJson.has("showBundleNames") ? requestJson.get("showBundleNames").asBoolean() : false;
	    	if (null == _context)
	    		return "OSGi Environment is not ready.";
	    	else{
	    		Bundle[] bundles = _context.getBundles();
	    		if (null == bundles)
	    			return "OSGi bundles is not ready.";
	    		else{
	    			if (!updateOSGiBundle(name, path))
	    				return "reload bundle failed.";
	    			else{
	    				if (showName){
		    				Object bds = ((DefaultOSGiFramework)_pluginFramework).getBundleNames(false, true).toString();
		    				logger.info(bds.toString());
		    				return bds;
	    				}
	    				return null;
	    			}
	    		}
	    	}
    	}else{
    		return "Need fields:    [bundleName,bundlePath]";
    	}
    }
}
