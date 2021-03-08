package com.meowlomo.vmc.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.exception.VMCJsonSchemaException;

public class ElementItem {

	protected String id;
	protected String elementType;
	protected String elementName;
	protected String locatorType;
	protected String locatorValue;

	protected ElementItem(String id, String type, String name) {
		this.id = id;
		this.elementType = type;
		this.elementName = name;
	}

	protected ElementItem(String id, String type, JsonNode elementNode) throws VMCJsonSchemaException {
		this.id = id;
		this.elementType = type;

		elementName = elementNode.get("name").asText();
		locatorType = elementNode.get("locatorType").asText();
		if ("URL".equalsIgnoreCase(locatorType)) {
			JsonNode parameter = elementNode.get("parameter");
			if (parameter.has("url"))
				locatorValue = parameter.get("url").asText();
			else if (parameter.has("baseUrl"))
				locatorValue = parameter.get("baseUrl").asText();
			else
				throw new VMCJsonSchemaException("locator type is URL,but doesn't has url or baseUrl.");
		} else {
			locatorValue = elementNode.get("locatorValue").asText();
		}
	}

	public static ElementItem genElementItem(boolean isDriver, String id, String driverType, JsonNode elementNode)
			throws VMCJsonSchemaException {
//		boolean isDriver = elementNode.get("isDriver").asBoolean();

		String type = elementNode.get("type").asText();
		return isDriver ? (new ElementDriverItem(id, driverType, type, elementNode)) : (new ElementItem(id, type, elementNode));
	}

	public JsonNode asJson() {
		ObjectNode element = JsonNodeFactory.instance.objectNode();
		element.put("id", id);
		element.put("elementType", elementType);
		element.put("elementName", elementName);
		element.put("locatorType", locatorType);
		element.put("locatorValue", locatorValue);

		return (JsonNode) element;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ElementItem))
			return false;
		if (this == obj)
			return true;
		ElementItem instance = (ElementItem) obj;

		return id.equals(instance.id) && elementType.equals(instance.elementType)
				&& elementName.equals(instance.elementName) && locatorType.equals(instance.locatorType)
				&& locatorValue.equals(instance.locatorValue);
	}

	public String id() {
		return id;
	}

	public String elementType() {
		return elementType;
	}

	public String elementName() {
		return elementName;
	}

	public String elementLocatorType() {
		return locatorType;
	}

	public String locatorValue() {
		return locatorValue;
	}
}
