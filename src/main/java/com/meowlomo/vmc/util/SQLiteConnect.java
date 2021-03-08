package com.meowlomo.vmc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowlomo.vmc.model.Task;

public class SQLiteConnect {
	/**
	 * Connect to a sample database
	 */
	public static void cacheTaskAtLocal(Task task) {
		Connection conn = null;
		try {
			// db parameters
			String url = "jdbc:sqlite:C:/sqlite/task.db";
			// create a connection to the database
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
			Statement stat = conn.createStatement();
			stat.executeUpdate("create table if not exists task(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,task TEXT(20971520),time INTEGER);");
			String jsonString = new ObjectMapper().writeValueAsString(task);
			stat.executeUpdate("insert into task(name,task,time) values('" + task.getName()+ "','" + jsonString + "',strftime('%s', 'now'));");
			ResultSet rs = stat.executeQuery("select count(id) tasknum from task;");
			ResultSetMetaData metaData = rs.getMetaData();
			if (rs.next()) {
				Object taskCount = rs.getObject(1);
				System.out.println("Task select name:" + metaData.getColumnName(1));
				Integer tc = (Integer)taskCount;
				System.out.println("Task cached count:" + taskCount);
				if (tc > 10) {
					stat.executeUpdate("delete from task order by id asc limit 10;");
					System.out.println("Execute sql: delete from task order by id asc limit 10");
				}
			} else {
				System.out.println("Task cached count failed");
			}
			stat.close();
			
		} catch (SQLException | JsonProcessingException e) {
			System.out.println("Task cached count exception occured.");
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public static Object getByOrder(Integer taskIndex) {
		Connection conn = null;
		Task target = null;
		try {
			do {
				// db parameters
				String url = "jdbc:sqlite:C:/sqlite/task.db";
				// create a connection to the database
				conn = DriverManager.getConnection(url);
				System.out.println("Connection to SQLite has been established.");
				Statement stat = conn.createStatement();
				ResultSet rs = null;
				if (null == taskIndex) {
					return null;
				} else if (0 == taskIndex) {
					rs = stat.executeQuery("select count(id) tasknum from task;");
					if (null != rs && rs.next()) {
						Object taskCount = rs.getObject(1);
						stat.close();
						return taskCount;
					}
					stat.close();
					return "Empty";
				} else if (1 == taskIndex) {
					rs = stat.executeQuery("select id,name,task,time from task order by id desc limit 1 offset " + (taskIndex - 1));
				} else {
					rs = stat.executeQuery("select id,name,task,time from task order by id desc limit 1 offset " + (taskIndex - 1));
				}
				
				if (null != rs && rs.next()) {
					Object dbTask = rs.getObject(3);
					if (dbTask instanceof String)
						target = new ObjectMapper().readValue((String) dbTask, Task.class);
					System.out.println("Task in DB id:" + rs.getObject(1));
					System.out.println("Task in DB name:" + rs.getObject(2));
					System.out.println("Task in DB time:" + rs.getObject(4));
				} else {
					System.out.println("Task cached get failed");
				}
				stat.close();
			} while (false);

		} catch (SQLException | JsonProcessingException e) {
			System.out.println("Task cached getting exception occured.");
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return target;
	}
}
