package com.meowlomo.vmc.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ManagerRequestProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ManagerRequestProcessor.class);
	
	public static boolean executeManagerRequest(ObjectNode content){
		if(content.get("type") == null){
			logger.error("The manager request is missing [type] which is required.");			
			return false;
		}else{
			String type = content.get("type").asText();
			if(type.equals("command")){
				if(content.get("command") != null){
					String command = content.get("command").asText();
					List<String> commandTokens = new ArrayList<String>();
					Matcher m = Pattern.compile("([^\\\"]\\S*|\\\"\\\"|\\\".*?[^\\\\]\\\")\\s*").matcher(command);
					while (m.find())
						commandTokens.add(m.group(1));
					logger.info("Executing command ["+command+"]");
					logger.debug("Tokenlized command : ["+commandTokens+"]");
					//TODO
//					CommandProcessBuilder tpb = new CommandProcessBuilder(null, commandTokens, null);
//					int result = tpb.execute();
					int result = -1;
					if(result == 0){
						return true; 
					}else{
						return false;
					}					
				}else{
					logger.error("The manager request type [command] is is missing the command.");
					return false;
				}
			}else if (type.equalsIgnoreCase("json")){
				boolean bCanHandle = content.has("data");
				if (!bCanHandle)
					logger.error("The manager request type [json] is is missing the data field.");
				
				return bCanHandle;
			}
			else{
				logger.error("The manager request type ["+type+"] is not supported.");
				return false;
			}
		}
	}
}
