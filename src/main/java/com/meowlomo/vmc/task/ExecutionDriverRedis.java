package com.meowlomo.vmc.task;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.osgi.OSGiUtil;

public class ExecutionDriverRedis extends ExecutionDriver {
	
	String host;
	String port;
	String password;
	boolean cluster;
	
//	private Boolean pool;
//	private String redisName;
//	private String host;
//	private Boolean ssl;   
//	private String pwd;
//	private Integer port; 
//	private Integer timeout; 
//	private Integer connectionTimeout; 
//	private Integer soTimeout;
//	private Integer maxTotal;
//	private Integer maxIdle;
//	private Integer minIdle;
	
	protected ExecutionDriverRedis(JsonNode driverJson) {
		super(driverJson);
		
		JsonNode property = driverJson.get("property"); 
		host = property.get("host").asText();
		port = property.get("port").asText();
		password = property.has("password") ? property.get("password").asText() : "";
		cluster = property.has("Cluster") ? property.get("Cluster").asBoolean() : false;
	}
	
	//TODO 
	private ExecutionDriverRedis(JsonNode driver, boolean version){
		super(driver);
		JsonNode property = driver.get("property"); 
//	    redisName = driver.get("name").asText();
//	    pool = property.get("pool").asBoolean();
//	    host = property.get("host").asText();
//	    ssl = property.get("ssl").asBoolean();   
//	    pwd = property.get("pwd").asText();
//	    port = property.get("jdbcUrl").asInt(); 
//	    timeout = property.get("port").asInt(); 
//	    connectionTimeout = property.get("connectionTimeout").asInt(); 
//	    soTimeout = property.get("soTimeout").asInt();
//	    maxTotal = property.get("maxTotal").asInt();
//	    maxIdle = property.get("maxIdle").asInt();
//	    minIdle = property.get("minIdle").asInt();
	}

	@Override
	public boolean singleton() {
		return false;
	}
	
	@Override
	public boolean redis() {
		return true;
	}
	
	@Override
	public ObjectNode asJson() {
		ObjectNode json = super.asJson();
		
		json.put("driverHost", host);
		json.put("driverPort", port);
		if (StringUtils.isNotEmpty(password))
			json.put("driverPassword", password);
		json.put("driverCluster", cluster);

		//TODO 
//		json.put("pool", pool);
//		json.put("redisName", redisName);
//		json.put("host", host);
//		json.put("ssl", ssl);
//		json.put("pwd", pwd);
//		json.put("port", port);
//		json.put("timeout", timeout);
//		json.put("connectionTimeout", connectionTimeout);
//		json.put("soTimeout", soTimeout);
//		json.put("maxTotal", maxTotal);
//		json.put("maxIdle", maxIdle);
//		json.put("minIdle", minIdle);
		
		return json;
	}
	
//	@Override
//	public void setup(Set<String> types) {
//		if (types.contains("Redis")) {
//			if (null == password || password.isEmpty())
//				OSGiUtil.addRedis(name, host, Integer.valueOf(port), cluster);
//			else
//				OSGiUtil.addRedis(name, host, Integer.valueOf(port), password, cluster);
//		}
//	}
//
//	//TODO 
////	public void init() {
////		OSGiUtil.addRedis(pool, redisName, host, ssl, pwd, port, timeout, connectionTimeout, soTimeout, maxTotal, maxIdle, minIdle);
////	}
//	
//	@Override
//	public void clear(Set<String> types) {
//		if (types.contains("Redis"))
//			OSGiUtil.removeRedis(name);
//	}
}
