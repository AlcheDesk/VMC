package com.meowlomo.vmc.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class DataSourceClassNameFactory {
	private final static Map<String, String> dbType2DBClassName = new HashMap<String, String>(){
		private static final long serialVersionUID = 6209205236504786895L;
		{
//			put("mysql", 		"com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//			put("mysql", 		"com.mysql.jdbc.Driver");//driver class name for mysql,and the hikariaCP says should just use setJdbcUrl for mysql.
			put("mysql", 		"com.mysql.cj.jdbc.Driver");//code warning said that com.mysql.jdbc.Driver is deprecated.
			put("oracle", 		"oracle.jdbc.pool.OracleDataSource");
			put("postgresql", 	"org.postgresql.ds.PGSimpleDataSource");
			put("sqlserver", 	"com.microsoft.sqlserver.jdbc.SQLServerDataSource");
			put("mariadb", 		"org.mariadb.jdbc.MariaDbDataSource");
		}
	};
	
	public static String getDSClass(String type) {
		return dbType2DBClassName.get(type);
	}
	
	public static String getDSClassByJdbcUrl(String jdbcUrl) {
		if (StringUtils.isEmpty(jdbcUrl))
			return new String();
		Iterator<Map.Entry<String, String>> iter = dbType2DBClassName.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			String key = ":" + entry.getKey() + ":";
			if (jdbcUrl.toLowerCase().contains(key))
				return entry.getValue();
		}
		return new String();
	}
}
