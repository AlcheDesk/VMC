package com.meowlomo.vmc.listener;

import org.springframework.stereotype.Component;

@Component
public class TaskEventListener {
//	
//	@Autowired
//	TaskHandler process;
//	
//	@Autowired
//	RuntimeVariables vars;
//	
//	@EventListener
//	public void processTask(TaskReceivedEvent event){
//		Task targetTask = (Task)event.getSource();
//		process.processTask(targetTask, false);
//	}
//	
//	@EventListener
//	public void addDataSource(DataSourceEvent event){
//		
//		if (vars.bNewDataBase){
//	
//			String dbUrl = vars.dbUrl;
//			String dbUser = vars.dbUser;
//			String dbPwd = vars.dbPwd;
//			String dbClassName = vars.dbClassName;
//			String dbName = vars.dbName;
//			
//			vars.bNewDataBase = false;
//			vars.dbUrl = new String("");
//			vars.dbUser = new String("");
//			vars.dbPwd = new String("");
//			vars.dbClassName = new String("");
//			vars.dbName = new String("");
//			
//			OSGiUtil.innerAddDataSource(dbUrl, dbUser, dbPwd, dbClassName, dbName);
//		}
//	}
}
