package com.meowlomo.vmc.scheduler;

import java.io.IOException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.Constant;
import com.meowlomo.vmc.config.RetrofitApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.event.TaskHandler;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.model.Worker;
import com.meowlomo.vmc.retrofit.EmsManagerAPI;
import com.meowlomo.vmc.util.RestartRebootUtils;

@Component
public class TaskExecuteScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskExecuteScheduler.class);
	
	@Value("${meowlomo.vmc.worker.reboot-delay:5}")
    private int rebootDelay;
	
	@Autowired
	RuntimeVariables vars;
	
	@Autowired
	TaskHandler process;
	
	@Autowired
    RetrofitApi serverApi = new RetrofitApi();

	@Scheduled(fixedDelay = 1200)
	public void doTaskExecution() {
		if (vars.isWorking() && !vars.isTaskQueueEmpty()) {
			logger.warn("VMC is busy.Tasks waiting!!! Should check if it's in deadlock.");
			return;
		}
		Task targetTask = vars.getTask();
		process.processTask(targetTask);
	}
	
	@Scheduled(fixedDelay = 5000)
	public void doRebootExecution() {
		if (!vars.isReboot())
			return;

		if (vars.getAllTasks().size() != 0) {
			logger.info("Worker still have running task. will try 5 seconds later.");
		} else {
			if (!vars.standSingleton() && vars.isRegisted()) {
				logger.info("Worker still have running task. will change worker status to DOWN to prepare reboot.");
				if (!vars.standSingleton() && vars.isRegisted()) {
                    Worker sendWorker = new Worker();
                    sendWorker.setStatus("DOWN");
					serverApi.createEmsService(EmsManagerAPI.class).updateWorkerToManager(vars.getUuid().toString(), sendWorker);
				}
			}
			
			try {
				// reboot VMC
				RestartRebootUtils.reboot(rebootDelay);
			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
