package com.meowlomo.vmc.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ArrayNode;

@JsonInclude(Include.NON_NULL)
public class DubboGenericServiceCallParam {
	public String zkHost;
	public String zkPort;
	public String interfaceVersion;
	public String interfaceClass;
	public String methodName;
	public ArrayNode parameter;
}
