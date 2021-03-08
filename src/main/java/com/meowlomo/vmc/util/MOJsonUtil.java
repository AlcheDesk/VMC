package com.meowlomo.vmc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MOJsonUtil {
	public static String REAL_NULL = "mo.json.null";
	public static String STRUCTURE_NODE = "mo.json.structure.node";
	
	public static String getText(ObjectNode json, String field){
		if (null == field || field.isEmpty())
			return null;
		JsonNode node = json.get(field);
		if (null == node)
			return null;
		if (node.isNull())
			return null;
		if (node instanceof NullNode)
			return REAL_NULL;
		
		return node.asText();
	}
	
	public static Boolean has(ObjectNode json, String field){
		return json.has(field);
	}
	
	public static Boolean valueNode(ObjectNode json, String field){
		return json.has(field) && !json.get(field).isContainerNode();
	}
	
	public static String getJsonValue(ObjectNode json, String field, String backupField, String backupValue, String notWantedValue) {
		if (json.has(field)) {
			if (json.get(field).asText().equals(notWantedValue)) {
				return backupValue;
			} else {
				return json.get(field).asText();
			}
		} else {
			if (null != backupField && !backupField.isEmpty() && json.has(backupField)) {
				if (json.get(backupField).asText().equals(notWantedValue)) {
					return backupValue;
				} else {
					return json.get(backupField).asText();
				}
			}
			
			return backupValue;
		}
	}
	
	public static String getJsonValue(JsonNode json, String field, String backupField, String backupValue, String notWantedValue) {
		return getJsonValue((ObjectNode)json, field, backupField, backupValue, notWantedValue);
	}
	
	public static String getDataSourceClassName(JsonNode jdbc, String dbUrl) {
//		JsonNode ds = jdbc.get("dataSourceClassName");
		return DataSourceClassNameFactory.getDSClassByJdbcUrl(dbUrl);
	}
}
