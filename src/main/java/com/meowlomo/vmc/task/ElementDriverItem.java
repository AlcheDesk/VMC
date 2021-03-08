package com.meowlomo.vmc.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.exception.VMCJsonSchemaException;
import com.meowlomo.vmc.exception.VMCTaskElementDriverNotMatchException;

public class ElementDriverItem extends ElementItem{
	ExecutionDriver driver;
	protected ElementDriverItem(String id, String driverType, String type, JsonNode elementNode) throws VMCJsonSchemaException {
		super(id, type, elementNode.get("name").asText());
		
//		String driverType = elementNode.get("driverType").asText();
		driver = TestCaseExecutor.testCase.driverList.getDriverByDriverType(driverType);
		if (null == driver) {
			System.err.println(driverType);
			throw new VMCTaskElementDriverNotMatchException("Task need driver type:[" + driverType + "],that means task shouldn't be dispatched to executor.");
		}
	}
	
	public String driverType() {
		return driver.type;
	}
	
	@Override
	public JsonNode asJson() {
		ObjectNode element = JsonNodeFactory.instance.objectNode();
		element.put("id", id);
		element.put("elementType", elementType);
		element.put("elementName", elementName);
		if (elementType.equals("WebBrowser")) {
			element.put("locatorType", driverType());
			element.put("locatorValue", driver.vendorName);
		}else {
			element.put("locatorType", locatorType);
			element.put("locatorValue", locatorValue);
		}
		
		ObjectNode driverJson = driver.asJson();
		element.setAll(driverJson);
		
		return (JsonNode)element;
	}
}
