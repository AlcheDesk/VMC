package com.meowlomo.vmc.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class ExecutionDriver {

	int id;
	String name;
//	String parameter;
	String type;
//	int runId;
	String vendorName;
	
	public String driverType() {return type;}
	public boolean singleton() {return false;}
	public boolean browser() {return false;}
	public boolean jdbc() {return false;}
	public boolean redis() {return false;}
	public boolean rpcDubbo() {return false;}
	
	protected ExecutionDriver(JsonNode driverJson) {
		id = driverJson.get("id").asInt();
		name = driverJson.get("name").asText();
//		parameter = driverJson.get("parameter").asText();
		type = driverJson.get("type").asText();
//		runId = driverJson.get("runId").asInt();
		vendorName = driverJson.get("vendorName").asText();
	}
	
	public static ExecutionDriver genDriver(JsonNode driverJson) {
		switch(driverJson.get("type").asText()) {
		case "WebBrowser":
		case "ChromeHeadless":	//TODO DriverType is ChromeHeadless,instruction type is VirtualWebBrowser or VirtualWebFunction
			return new ExecutionDriverWebBrowser(driverJson);
		case "JDBC":
			return new ExecutionDriverJDBC(driverJson);
		case "API":
			return new ExecutionDriverRestAPI(driverJson);
		case "Redis":
			return new ExecutionDriverRedis(driverJson);
		case "ZooKeeper":
			return new ExecutionDriverZooKeeper(driverJson);
		case "Email":
			return new ExecutionDriverEmail(driverJson);
		case "Storage":
		case "APP":
		case "Tablet":
		default:
			return null;
		}
	}
	
	public ObjectNode asJson() {
		ObjectNode json = JsonNodeFactory.instance.objectNode();
		
		json.put("id", id);
		json.put("driverName", name);
//		json.put("driverParameter", parameter);
//		json.put("driverRunId", runId);
		json.put("driverVendorName", vendorName);
		
		return json;
	}
}
