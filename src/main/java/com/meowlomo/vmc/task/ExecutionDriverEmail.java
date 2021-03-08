package com.meowlomo.vmc.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExecutionDriverEmail extends ExecutionDriver {

	static final Logger logger = LoggerFactory.getLogger(ExecutionDriverEmail.class);
	String account;
	String password;
	String host;
	String port;
	String storeType;
	
	protected ExecutionDriverEmail(JsonNode driver) {
		super(driver);
		JsonNode property = driver.get("property");
		account = property.get("Account").asText();
		password = property.get("Password").asText();
		host = property.get("ServerHost").asText();
		port = property.get("ServerPort").asText();
		storeType = property.get("StoreServerType").asText();
	}
	
	@Override
	public ObjectNode asJson() {
		ObjectNode json = super.asJson();
		
		json.put("emailHost", host);
		json.put("emailPort", port);
		json.put("emailAccount", account);
		json.put("emailPassword", password);
		json.put("emailStoreType", storeType);
		
		return json;
	}
	
//	@Override
//	public void setup(Set<String> types) {
//		if (types.contains(type)) {//"ZooKeeper"
//			if (StringUtils.isNotEmpty(zkHost) && StringUtils.isNotEmpty(zkPort))
//				OSGiUtil.setupZooKeeper(zkHost, zkPort);
//			else
//				MOLogger.errorDouble(logger, "ZooKeeper Driver properties are not enough, host:" + zkHost + ", port:" + zkPort);
//		}
//	}
//
//	@Override
//	public void clear(Set<String> types) {
//		if (types.contains(type))//"ZooKeeper"
//			OSGiUtil.clearZooKeeper();
//	}
}
