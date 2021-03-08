package com.meowlomo.vmc.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExecutionDriverZooKeeper extends ExecutionDriver {

	static final Logger logger = LoggerFactory.getLogger(ExecutionDriverZooKeeper.class);
	String zkHost;
	String zkPort;
	
	protected ExecutionDriverZooKeeper(JsonNode driver) {
		super(driver);
		JsonNode property = driver.get("property"); 
		zkHost = property.get("host").asText();
		zkPort = property.get("port").asText();
	}

	@Override
	public boolean rpcDubbo() {
		return true;
	}
	
	@Override
	public ObjectNode asJson() {
		ObjectNode json = super.asJson();
		
		json.put("zkHost", zkHost);
		json.put("zkPort", zkPort);
		
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
