package com.meowlomo.vmc.task;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExecutionDriverWebBrowser extends ExecutionDriver{
	String windowSize; // like 1366x768
	String mobileEmulation;// like iPhone 6/7/8 Plus
	boolean browserConnectionEnabled;
	
	ExecutionDriverWebBrowser(JsonNode driver){
		super(driver);
		JsonNode property = driver.get("property");
		
		if (NullNode.instance == property) {
			windowSize = "1366x768";
			browserConnectionEnabled = true;
			mobileEmulation = "";
		} else {

			if (property.has("window.size")) {
				windowSize = property.get("window.size").asText();
			} else {
				windowSize = "1366x768";
			}
			
			if (property.has("mobileEmulation")) {
				mobileEmulation = property.get("mobileEmulation").asText();
			} else {
				mobileEmulation = "";
			}

			if (property.has("browserConnectionEnabled")) {
				browserConnectionEnabled = driver.get("property").get("browserConnectionEnabled").asBoolean();
			} else {
				browserConnectionEnabled = true;
			}
		}
	}

	@Override
	public boolean singleton() {
		return true;
	}
	
	@Override
	public boolean browser() {
		return true;
	}
	
	@Override
	public ObjectNode asJson() {
		ObjectNode json = super.asJson();
		
		json.put("driverWindowSize", windowSize);
		json.put("driverBrowserConnectionEnabled", browserConnectionEnabled);
		if (StringUtils.isNotEmpty(mobileEmulation)) {
			json.put("mobileEmulation", mobileEmulation);
		}
		
		return json;
	}
}
