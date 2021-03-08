package com.meowlomo.vmc.service.impl;

import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meowlomo.vmc.config.RetrofitApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.service.WorkerUtilService;

@Service
public class WorkerUtilServiceImpl implements WorkerUtilService{
    
    private final Logger logger = LoggerFactory.getLogger(WorkerUtilServiceImpl.class);

    @Autowired
    RuntimeVariables runtimeVariables = new RuntimeVariables();
    
//    @Autowired
//    private MappingJackson2HttpMessageConverter jacksonConverter;
    
    @Autowired
    RetrofitApi retrofitApi = new RetrofitApi();
    
    @Override
    public boolean prepareToKillCurrentTask(Task checkTask) {
        //get the current working task first
        ArrayList<Task> currentTasks = runtimeVariables.getAllTasks();
        //loop the task an compare the uuid of the task to check id the task exists in the current executing tasks.
        boolean foundTheTask = false;
        Task targetTask = null;
        for (Task task : currentTasks) {
            if (task.getUuid().equals(checkTask.getUuid())) {
                foundTheTask = true;
                targetTask = task;
                break;
            }
        }
        //stick the worker to working mode
        if (foundTheTask && targetTask != null) {
            RuntimeVariables.HOLD_ON_WORKING_MODE_FOR_REBOOT = true;
        }
        else {
            return true;
        }

//        try {
//            //get the run object from the task
//            JsonNode runData = targetTask.getData();
//            Run targetRun = jacksonConverter.getObjectMapper().treeToValue(runData, Run.class);
//            Long runId = targetRun.getId();
//            //update the log if needed to the ATM
//            Run updateRun = new Run();
//            updateRun.setLog("Status will be updated to ERROR due to termination signal.");
//            updateRun.setStatus("TERMINATED");
//            AtmManagerAPI atmApiService = retrofitApi.createAtmService(AtmManagerAPI.class);
//            Call<MeowlomoResponse> atmCall = atmApiService.updateRun(runId, updateRun);
//            Response<MeowlomoResponse> atmResponse = atmCall.execute();
//            if (atmResponse.isSuccessful()) {
//                //the run status has been updated. we just skip this.
//            }
//            else {
//                //error, we need to stop this
//                logger.error("Error on updating run status to ERROR for termination signal.");
//                return false;
//                
//            }
//        } catch (JsonProcessingException e) {
//            logger.error("Exception on check status for task termination ["+e.getMessage()+"]", e);
//            return false;
//        } catch (IOException e) {
//            logger.error("Exception on check status for task termination ["+e.getMessage()+"]", e);
//            return false;
//        }
//
//        try {
//            //update the log if needed to the EMS
//            //get the uuid of the task first
//            UUID taskUuid = checkTask.getUuid();
//            Long taskId = checkTask.getId();
//            Task updateTask = new Task();
//            updateTask.setUuid(taskUuid);
//            updateTask.setId(taskId);
//            updateTask.setStatus("TERMINATED");
//            TaskLog taskLog = new TaskLog();
//            taskLog.setMessage("Status will be updated to ERROR due to termination signal.");
//            updateTask.setLogs(Collections.singletonList(taskLog));
//            EmsManagerAPI emsApiService = retrofitApi.createEmsService(EmsManagerAPI.class);
//            Call<MeowlomoResponse> emsCall = emsApiService.updateTaskToManager(new Task[] {updateTask});
//            Response<MeowlomoResponse> emsResponse = emsCall.execute();
//            if (emsResponse.isSuccessful()) {
//                //the run status has been updated. we just skip this.
//            }
//            else {
//                //error, we need to stop this
//                logger.error("Error on updating task status to ERROR for termination signal.");
//                return false;
//                
//            }
//        } catch (IOException e) {
//            logger.error("Exception on check status for task termination ["+e.getMessage()+"]", e);
//            return false;
//        }
        
        return true;
    }

    @Override
    public boolean checkToken(String tokenString) {
    	if (true)
    		return true;
    	
    	//TODO
        //check the id
        if (tokenString == null) {
            logger.error("token check the input worker string is null.");
            return false;
        }
        else {
            //check the token from the worker
            UUID workerCurrentToken = RuntimeVariables.token;
            if (workerCurrentToken == null) {
                logger.error("Worker token in database is null, it is not registered properly.");
                return false;
            }
            else if (workerCurrentToken.compareTo(UUID.fromString(tokenString)) == 0) {
                return true;
            }
            else {
                logger.debug("Worker token check no valid. input token {}  , actual token {}", tokenString, workerCurrentToken);
                return false;
            }           
        }
    }

}
