package com.meowlomo.vmc.event;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.Constant;
import com.meowlomo.vmc.config.RetrofitApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.core.ServerCommunicator;
import com.meowlomo.vmc.file.FileServerCommunicator;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.task.TaskRunner;
import com.meowlomo.vmc.util.CommunicationUtils;
import com.meowlomo.vmc.util.MOCommonUtil;

@Component
public class TaskHandler {
	private final Logger logger = LoggerFactory.getLogger(TaskHandler.class);

	@Autowired
	RetrofitApi serverApi;
	public RetrofitApi getRetrofitApi() {
		return serverApi;
	}

	@Autowired
	RuntimeVariables runtimeVariables;
	public RuntimeVariables getRuntimeVariables() {
		return runtimeVariables;
	}

	@Autowired
	ServerCommunicator serverCommunicator;
	public ServerCommunicator getServerCommunicator() {
		return serverCommunicator;
	}
	
	@Autowired
	FileServerCommunicator fileServerCommunicator;
	public FileServerCommunicator getFileServerCommunicator() {
		return fileServerCommunicator;
	}
	
	@Autowired
	CommunicationUtils communicationUtils;
	public CommunicationUtils getCommunicationUtils() {
		return communicationUtils;
	}

	@Async
	public String processTask(Task task) {
//	public Future<String> processTask(Task task) {
		if (null == task) return Constant.NULL_TASK;//return new AsyncResult<String>("0");
		
		try{
			String taskTip = genTaskTip(task);
			logger.info("begin processing " + taskTip);
			boolean interactive = task.getInteractive();
			runtimeVariables.registerTaskBeginProcess(task);
			MOCommonUtil.sleepSeconds(1);
		
			if (interactive) {
				if (runtimeVariables.isWorking()) {
					logger.error("a GUI task has been assigned. Where VMC is working." + taskTip);
				} else if (!runtimeVariables.inGUIProcessing()) {
					// TODO 当没有GUI类型的独占用例时，可以考虑进行非GUI任务下发，以提高资源利用率
					doGUITaskJob(task, " No waiting." + taskTip);
				} else {
					//TODO 循环等待这块需要修改,目前的@Async不会让此处并发重入,既然如此，则不需要这块循环代码
					// do a wait loop to prevent server delay, 20 s
					int loop = 0;
					while (runtimeVariables.inGUIProcessing() && loop ++ < 4) MOCommonUtil.sleepSeconds(5);;
	
					// check again
					if (runtimeVariables.inGUIProcessing()) {
						reportBadAssignmentCase1(task);
					} else {
						doGUITaskJob(task, " start [" + taskTip + "] after 20s waiting.");
					}
				}
			} else {
				if (runtimeVariables.inGUIProcessing()) {
					reportBadAssignmentCase2(task);
				} else {
					doNonGUITask(task);
				}
			}
		}catch(Exception e){
			logger.error("Exception occured in test case's processing, type is:" + e.getClass().getName());
		}finally{
			runtimeVariables.setInGUIProcessing(false);
			runtimeVariables.unRegisterTaskEndingProcess(task);
			runtimeVariables.updateWorkerStatusSpringBootWay();
		}
		return "OK";//new AsyncResult<String>("1");
	}

	protected String genTaskTip(Task task) {
		String taskName = task.getName();
		UUID taskUUID = task.getUuid();
		String taskTip = String.format("The task name : %s, uuid : %s", taskName, taskUUID);
		return taskTip;
	}

	private void doGUITaskJob(Task task, String taskTip) {
		try {
			logger.info("begin processing an interactive task." + taskTip);
			doGUITask(task);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			runtimeVariables.setInGUIProcessing(false);
		}
	}

	/**
	 * report back the ems, about a wrong assignment, while an interactive task
	 * in processing, and another interactive task assigned.
	 * 
	 * @param taskID
	 * @param taskUUID
	 */
	private void reportBadAssignmentCase1(Task currentTask) {
		logger.error("Worker has been assigned mutiple interactive tasks at the same time. New task [" + currentTask.getUuid()
				+ "] will be dropped.");

		String msg = "Multi interactive tasks have been assign to the worker at the same time. Please contact system admin.";
		String taskStatusLog = "Dropped by worker [" + runtimeVariables.getUuid() + "] with message \n" + "[" + msg
				+ "] \n Updated to [ERROR]";
		
		communicationUtils.doTaskUpdateToEMS(taskStatusLog, "ERROR", currentTask);
	}

	/**
	 * report back the ems, about a wrong assignment, while an interactive task
	 * in processing, and another non-interactive task assigned.
	 * 
	 * @param taskUUID
	 */
	private void reportBadAssignmentCase2(Task currentTask) {
		logger.error("Worker has been assigned a non-interactive tasks where an interactive task in "
				+ "processing at the same time. New task [" + currentTask.getUuid() + "] will be dropped.");
		
		String msg = "An interactive task has been assigned to the worker while another non-interactive task assigned."
				+ " Please contact system admin.";
		String taskStatusLog = "Dropped by worker [" + runtimeVariables.getUuid() + "] with message \n" + "[" + msg
				+ "] \n Updated to [ERROR]";
		
		communicationUtils.doTaskUpdateToEMS(taskStatusLog, "ERROR", currentTask);
	}
	
	private void doNonGUITask(Task task){
		doTaskJobInner(task, false);
	}
	
	private void doGUITask(Task task){
		doTaskJobInner(task, true);
	}
	
	private void doTaskJobInner(Task task, boolean bInteractive) {
		if (bInteractive) {
			runtimeVariables.setInGUIProcessing(true);
		} else {
			runtimeVariables.updateNonTaskProcessingStatus(task);
		}

		new TaskRunner(this, task).run();
	}
}
