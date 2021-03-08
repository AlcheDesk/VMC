package com.meowlomo.vmc.task;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.osgi.OSGiUtil;
import com.meowlomo.vmc.util.MOJsonUtil;

public class ExecutionDriverJDBC extends ExecutionDriver{
	String dataSourceClassName; //
	String jdbcUrl;
	String password;
	String username;
	
	
	ExecutionDriverJDBC(JsonNode driver){
		super(driver);
		
		JsonNode property = driver.get("property"); 
		jdbcUrl = property.get("jdbcUrl").asText();
		password = property.get("password").asText();
		username = property.get("username").asText();
		dataSourceClassName = MOJsonUtil.getDataSourceClassName(property, jdbcUrl);//property.get("dataSourceClassName").asText();
	}

	@Override
	public boolean singleton() {
		return false;
	}
	
	@Override
	public boolean jdbc() {
		return true;
	}
	
	@Override
	public ObjectNode asJson() {
		ObjectNode json = super.asJson();
		
		json.put("driverDataSourceClassName", dataSourceClassName);
		json.put("driverJdbcUrl", jdbcUrl);
		json.put("driverPassword", password);
		json.put("driverUsername", username);
		
		return json;
	}
	
//	@Override
//	public void setup(Set<String> types) {
//		if (types.contains("SQL"))
//			OSGiUtil.innerAddDataSource(jdbcUrl, username, password, dataSourceClassName, name);
//		//why not OSGiUtil.addDataSource
//	}
//	
//	@Override
//	public void clear(Set<String> types) {
//		if (types.contains("SQL"))
//			OSGiUtil.removeDataSource(name);
//	}
}
