package com.meowlomo.vmc.util;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.core.ServerCommunicator;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.model.TaskLog;
import com.meowlomo.vmc.osgi.OSGiUtil;

@Component
public class CommunicationUtils {

	@Autowired
	ServerCommunicator serverCommunicator;
	
	@Autowired
	RuntimeVariables vars;
	
	public void doTaskUpdateToEMS(String msg, String status, Task task){
		Task taskUpdate = new Task();
		taskUpdate.setStatus(status);
		
		taskUpdate.setParameter(task.getParameter());
		taskUpdate.setData(task.getData());
		taskUpdate.setExecutionResult(task.getExecutionResult());
		
		TaskLog log = new TaskLog();
		log.setMessage(msg);
		taskUpdate.setLogs(new ArrayList<TaskLog>() {
			private static final long serialVersionUID = -3299102960923362399L;
			{
				add(log);
			}
		});
		serverCommunicator.updateTaskToManager(task.getUuid(), taskUpdate);//taskUUID
//		removeDataSourceIfNeeded();
	}
	
	public String genThreadName(Task task) {
		UUID taskUUID = task.getUuid();
		String threadName = "Task UUID: " + taskUUID.toString();
		return threadName;
	}
	
//	private void removeDataSourceIfNeeded(){
//		if (vars.bNewDataBase){
//			vars.bNewDataBase = false;
//			OSGiUtil.removeDataSource(vars.dbName);
//		}
//	}
}
