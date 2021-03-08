package com.meowlomo.vmc.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.util.JsonNodeUtils;

public class ExecutionDriverRestAPI extends ExecutionDriver {

	long ConnectionRequestTimeout;
	long ConnectTimeout;
	long SocketTimeout;
	boolean ExpectContinueEnabled;
	
	protected ExecutionDriverRestAPI(JsonNode driverJson) {
		super(driverJson);
		JsonNode property = driverJson.get("property");
		ConnectionRequestTimeout = JsonNodeUtils.getNullWithNon(property, "ConnectionRequestTimeout1").asLong(60);
		ConnectTimeout = JsonNodeUtils.getNullWithNon(property, "ConnectTimeout").asLong(60);
		SocketTimeout = JsonNodeUtils.getNullWithNon(property, "SocketTimeout").asLong(60);
		ExpectContinueEnabled = JsonNodeUtils.getNullWithNon(property, "ExpectContinueEnabled").asBoolean(false);
	}
	
	@Override
	public boolean singleton() {
		return true;
	}
	
	@Override
	public ObjectNode asJson() {
		ObjectNode json = super.asJson();
		
		json.put("ConnectionRequestTimeout", ConnectionRequestTimeout);
		json.put("ConnectTimeout", ConnectTimeout);
		json.put("SocketTimeout", SocketTimeout);
		json.put("ExpectContinueEnabled", ExpectContinueEnabled);
		
		return json;
	}
}
