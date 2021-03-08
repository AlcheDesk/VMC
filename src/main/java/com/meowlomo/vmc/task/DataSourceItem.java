package com.meowlomo.vmc.task;

public class DataSourceItem {
	
	protected String username;
	protected String password;
	protected String dsName;
	protected String jdbcUrl;
	protected String dataSourceClassName;
	
	public DataSourceItem(String user, String pwd, String name, String url, String dbClass) {
		username = user;
		password = pwd;
		dsName = name;
		jdbcUrl = url;
		dataSourceClassName = dbClass;
	}
	
	public String[] toArray(){
		String[] fields = new String[5];
		fields[0] = username;
		fields[1] = password;
		fields[2] = dsName;
		fields[3] = jdbcUrl;
		fields[4] = dataSourceClassName;
		
		return fields;
	}
}
