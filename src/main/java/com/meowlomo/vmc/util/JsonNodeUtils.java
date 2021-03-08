package com.meowlomo.vmc.util;

import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;

public class JsonNodeUtils {
	public static Boolean aValidKey(JsonNode json, String key) {
		if (null == key || key.isEmpty() || null == json)
			return Boolean.FALSE;
		else {
			return json.has(key) && null != json.get(key) && NullNode.instance != json.get(key);
		}
	}
	
	public static JsonNode getNullWithNon(JsonNode json, String key) {
		if (aValidKey(json, key))
			return json.get(key);
		else
			return NullNode.instance;
	}

	/**
	 * 判断是否是int型的id.  0不是，0123也不是。不判断是否是32位整型
	 * @param str
	 * @return Boolean
	 */
	public static Boolean isIntegerID(String testStr) {
		if (null == testStr || testStr.isEmpty()) return false;
		
		Pattern pattern = Pattern.compile("[1-9]{1}[0-9]*");
	    return pattern.matcher(testStr).matches();
	}
	
	public static String genOptionsStr(JsonNode instruction) {
		String options = "";
		if (JsonNodeUtils.aValidKey(instruction, "instructionOptions")){
			//TODO a filter call, then a join call will be simple
			ArrayNode instructionOptions = (ArrayNode) instruction.withArray("instructionOptions");
			if (instructionOptions.iterator().hasNext()){
				for(JsonNode o : instructionOptions){
					MOObjectNode option = MOObjectNode.copyNode(o);
					if (JsonNodeUtils.aValidKey(option, "name")){
						String optionStr = option.get("name").asText();
						if (null != optionStr && !optionStr.isEmpty()){
							if (JsonNodeUtils.aValidKey(option, "valueRequired") && option.get("valueRequired").isBoolean() 
									&& option.get("valueRequired").asBoolean()){
								String optionsValue = option.get("value").asText();
								options += ";" + optionStr + ":" + optionsValue;
							}else{
								options += ";" + optionStr;
							}
						}
					}
				}
			}
			if (options.startsWith(";"))
				options = options.substring(1, options.length());
		}
		return options;
	}
}
