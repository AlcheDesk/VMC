package com.meowlomo.vmc.util;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.exception.VMCJsonFieldNotExistException;

public class MOObjectNode extends ObjectNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MOObjectNode(JsonNodeFactory nc) {
		super(nc);
	}

	/**
	 * note: 由于package原因,无法直接使用ObjectNode中的protected域成员,一种使用方式是将此extends类放到声明为
	 * com.fasterxml.jackson.databind.node的package中,为保持位置一致，此法不用
	 * 
	 * @param prototype
	 * @return
	 */
	public static MOObjectNode copyNode(ObjectNode prototype) {
		//TODO 不稳定的方式，基于JsonNodeFactory.instance的单例为全局唯一
		MOObjectNode ret = new MOObjectNode(JsonNodeFactory.instance);
		Iterator<String> iter = prototype.fieldNames();
		while(iter.hasNext()) {
			String key = iter.next();
			JsonNode value = prototype.get(key);
			ret._children.put(key, value.deepCopy());
		}

		return ret;
	}

	public static MOObjectNode copyNode(JsonNode prototype) {
		//TODO 不稳定的方式，基于JsonNodeFactory.instance的单例方式
		MOObjectNode ret = new MOObjectNode(JsonNodeFactory.instance);
		Iterator<String> iter = prototype.fieldNames();
		while(iter.hasNext()) {
			String key = iter.next();
			JsonNode value = prototype.get(key);
			ret._children.put(key, value.deepCopy());
		}

		return ret;
	}
	
    @Override
    public MOObjectNode deepCopy() {
		MOObjectNode ret = new MOObjectNode(_nodeFactory);

        for (Map.Entry<String, JsonNode> entry: _children.entrySet())
            ret._children.put(entry.getKey(), entry.getValue().deepCopy());

        return ret;
    }

    public static MOObjectNode newInstance() {
    	return new MOObjectNode(JsonNodeFactory.instance);
    }
    
    @Override
    public JsonNode get(String fieldName) {
    	if (fieldName == null) {
            throw new VMCJsonFieldNotExistException("Null key in Json Object get.");
        }
        JsonNode object = _children.get(fieldName);
        if (object == null) {
            throw new VMCJsonFieldNotExistException("Json Object Key [" + fieldName + "] not found.");
        }
        return object;
    }
    
    @Override
    public boolean has(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) return false;
        return _children.containsKey(fieldName);
    }
}
