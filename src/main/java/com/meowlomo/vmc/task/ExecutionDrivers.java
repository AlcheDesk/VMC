package com.meowlomo.vmc.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.exception.VMCLogicalException;
import com.meowlomo.vmc.util.MOObjectNode;

public class ExecutionDrivers {
	public final static String JDBC = "JDBC";
	public final static String REDIS = "Redis";
	public final static String ZK = "ZooKeeper";
	
	//APP,Tablet,JDBC,API,Storage,WebBrowser
	protected Map<String, ExecutionDriver> drivers = new HashMap<String, ExecutionDriver>();
	
	public ExecutionDrivers(ArrayNode driverArray) throws VMCLogicalException {
		if (null != drivers) {
			for(JsonNode driver : driverArray) {
				ExecutionDriver executionDriver = ExecutionDriver.genDriver(MOObjectNode.copyNode(driver));
				if (null != executionDriver) {
					if (drivers.containsKey(executionDriver.type)) {
						throw new VMCLogicalException(executionDriver.type + ":含有多个driver");
					}
					drivers.put(executionDriver.type, executionDriver);
				}
			}
		}
	}
	
	public ObjectNode asJSON() {
		MOObjectNode json = MOObjectNode.newInstance();
		Iterator<Map.Entry<String, ExecutionDriver>> iter = drivers.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, ExecutionDriver> entry = iter.next();
			ObjectNode driverJson = entry.getValue().asJson();
			json.set(entry.getKey(), driverJson);
		}
		return json;
	}
	
	public ExecutionDriver getDriverByDriverType(String typeName) {
		//TODO 
		return drivers.get(typeName);
	}
	
	public boolean hasJDBCDriver() {
		return drivers.containsKey(JDBC);
	}
	
	public boolean hasRedisDriver() {
		return drivers.containsKey(REDIS);
	}
	
	public boolean hasZooKeeperDriver() {
		return drivers.containsKey(ZK);
	}
	
//	public void setup(Set<String> types) {
//		for (Entry<String, ExecutionDriver> entry : drivers.entrySet()) {
//			//JDBC
//			entry.getValue().setup(types);
//		}
//	}
//	
//	public void clear(Set<String> types) {
//		for(Entry<String, ExecutionDriver> entry : drivers.entrySet()) {
//			entry.getValue().clear(types);
//		}
//	}
}
