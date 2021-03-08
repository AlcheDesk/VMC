package com.meowlomo.vmc.core;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowlomo.vmc.config.RetrofitApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.model.MeowlomoResponse;
import com.meowlomo.vmc.model.Run;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.model.Worker;
import com.meowlomo.vmc.retrofit.AtmManagerAPI;
import com.meowlomo.vmc.retrofit.EmsManagerAPI;
import com.meowlomo.vmc.util.MOLogger;

import retrofit2.Call;
import retrofit2.Response;

@Component
public class ServerCommunicator {

	private static final Logger logger = LoggerFactory.getLogger(ServerCommunicator.class);

	@Autowired
	RetrofitApi serverApi;
	
	@Autowired
	RuntimeVariables runtimeVariables;

	EmsManagerAPI emsManagerAPI = null;
	AtmManagerAPI atmManagerAPI = null;
	
	@EventListener(ApplicationReadyEvent.class)
	public void updateCommunicator(){
		if (null == this.emsManagerAPI)
			this.emsManagerAPI = serverApi.createEmsService(EmsManagerAPI.class, runtimeVariables.getAuthToken());
		
		if (null == this.atmManagerAPI)
			this.atmManagerAPI = serverApi.createAtmService(AtmManagerAPI.class, runtimeVariables.getAuthToken());
	}

	public boolean updateWorkerInfoToForeman(Worker worker){
		
		if (runtimeVariables.standSingleton()) {
		    logger.info("not registration to the EMS");
		    return true;
		}
		
		logger.info("Updating worker info "+worker.toString()+" to manager ");
		
		//check worker is registered
		if(runtimeVariables.getUuid() != null && runtimeVariables.isRegisted()){
			String status = worker.getStatus();
			logger.info("Updating worker status: [" + status +  "] to manager");
			Call<MeowlomoResponse> call = emsManagerAPI.updateWorkerToManager(runtimeVariables.getUuid().toString(), worker);
			Response<MeowlomoResponse> response;
			MeowlomoResponse reponseBody;
			try {
				response = call.execute();
				reponseBody = response.body();
			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
			if(response.isSuccessful()){
				//get the worker back
				ObjectMapper mapper = new ObjectMapper();
				List<Worker> workers = mapper.convertValue(reponseBody, new TypeReference<List<Worker>>(){});
				String statusFromDb = workers.get(0).getStatus();
				logger.info("Changing the status of worker in MultiThreadruntimeVariables to "+status);
				if(statusFromDb.equalsIgnoreCase(status)){
					//update the last successfully
					runtimeVariables.setLastCommunicationTime(new Date());
					return true;
				}else{
					logger.error("ForemanCommunicator::updateWorkerStatusToForeman::Status in DB is different, Expected in db "+status+" Actual "+statusFromDb);
					return false;
				}
			}else{
				logger.error("ForemanCommunicator::updateWorkerStatusToForeman::"+reponseBody.toString());
				return false;
			}
		}else{
			logger.error("ForemanCommunicator::updateWorkerStatusToForeman::Worker is not registered, update status should not be called. Check the log.");
			return false;
		}
	}

	public String getWorkerStatusFromForeman(){
		if (runtimeVariables.standSingleton())
			return "";
		
		logger.info("Getting worker status from manager ");
		//check worker is registered
		if(runtimeVariables.getUuid() != null && runtimeVariables.isRegisted()){	
			logger.info("Getting worker status from manager.");
			//				JSONObject jsonResponse = JSONHttpUtil.send(url,"get", null);
			Call<MeowlomoResponse> call = emsManagerAPI.getWorkerFromManager(runtimeVariables.getUuid().toString());
			Response<MeowlomoResponse> response;
			MeowlomoResponse reponseBody;
			try {
				response = call.execute();
				reponseBody = response.body();
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
			if(response.isSuccessful()){
				//get the worker back
				ObjectMapper mapper = new ObjectMapper();
				List<Worker> workers = mapper.convertValue(reponseBody, new TypeReference<List<Worker>>(){});
				String statusFromDb = workers.get(0).getStatus();
				return statusFromDb;
			}else {
				logger.error("ForemanCommunicator::getWorkerStatusFromForeman::error response from the server."+reponseBody.getError().toString());
				return null;
			}
		}else{
			logger.error("ForemanCommunicator::getWorkerStatusFromForeman::Worker[" + runtimeVariables.getUuid()+ "]is not registered, update status should not be called. Check the log.");
			return null;
		}
	}

	public boolean updateTaskToManager(UUID uuid, Task task){
		if (runtimeVariables.standSingleton())
			return true;
		
		updateCommunicator();
		
		//get the id from the task
		task.setUuid(uuid);
		Task[] taskArray = {task};
		Call<MeowlomoResponse> call = emsManagerAPI.updateTaskToManager(taskArray);
		Response<MeowlomoResponse> response;
		MeowlomoResponse reponseBody;
		try {
			response = call.execute();
			reponseBody = response.body();
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

		if(response.isSuccessful()){
			//update the last successfully
			runtimeVariables.setLastCommunicationTime(new Date());
			ObjectMapper mapper = new ObjectMapper();
			List<Task> tasks = mapper.convertValue(reponseBody.getData(), new TypeReference<List<Task>>(){});
			//check the field one by one
			boolean finalResult = true;

			//check the response status of the task
			if(!tasks.isEmpty()){
				String statusToUpdate = task.getStatus();
				String statusFromDb = tasks.get(0).getStatus();
				if(statusFromDb.equalsIgnoreCase(statusToUpdate)){
					//set the status of the task
					logger.info("Successfully update task status to " + statusToUpdate + ", with uuid:" + uuid);
				}else{
					logger.error("Status in DB is different, Expected in db: "+statusToUpdate+" Actual: "+statusFromDb);
					finalResult = false;
				}
			}else{
				logger.error("The response from the server doesn't contain the field [status]. :: "+reponseBody.toString());
				finalResult = false;
			}

			return finalResult;
		}else {
			try {
				MOLogger.errorDouble(logger, response.errorBody().string());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (null != reponseBody)
				logger.error("Server responded error ["+reponseBody.getError()+"]");
			else
				logger.error("Server responded error [reponseBody为空]");
			return false;
		}
	}

	public Worker getWorkerFromManager(){
		if (runtimeVariables.standSingleton())
			return null;
		
		//check worker is registered
		if(runtimeVariables.getUuid() != null && runtimeVariables.isRegisted()){

			logger.info("Getting worker status to manager");
			Call<MeowlomoResponse> call = emsManagerAPI.getWorkerFromManager(runtimeVariables.getUuid().toString());
			Response<MeowlomoResponse> response;
			MeowlomoResponse reponseBody;
			try {
				response = call.execute();
				reponseBody = response.body();
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
			if(response.isSuccessful()){
				runtimeVariables.setLastCommunicationTime(new Date());
				ObjectMapper mapper = new ObjectMapper();
				List<Worker> workers = mapper.convertValue(reponseBody, new TypeReference<List<Worker>>(){});
				if(!workers.isEmpty()) {
					return workers.get(0);
				}
				else {
					return null;
				}
			}else{
				logger.error("ForemanCommunicator::getWorkerInfoFromForeman::error response from the server."+reponseBody.getError());
				return null;
			}
		}else{
			logger.error("ForemanCommunicator::getWorkerInfoFromForeman::Worker is not registered, get worker info should not be called. Check the log.");
			return null;
		}
	}
	
	public String getWorkerIPFromManager() {
		if (runtimeVariables.standSingleton())
			return null;
		
		logger.info("Getting worker ip from manager");
		if (null == emsManagerAPI)
			emsManagerAPI = serverApi.createEmsService(EmsManagerAPI.class, runtimeVariables.getAuthToken());
		Call<MeowlomoResponse> call = emsManagerAPI.getWorkerSelfIP();
		Response<MeowlomoResponse> response;
		MeowlomoResponse reponseBody;
		try {
			response = call.execute();
			reponseBody = response.body();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		if(response.isSuccessful()){
			runtimeVariables.setLastCommunicationTime(new Date());
			JsonNode responseJson = reponseBody.getMetadata();
			if(null != responseJson && responseJson.has("remoteAddr")) {
				return responseJson.get("remoteAddr").asText();
			}
			else {
				return null;
			}
			
		}else{
			logger.error("getWorkerIPFromManager::error response from the server."+reponseBody.getError());
			return null;
		}
	}
	
	public String directFinishRun(Run[] run) {
		if (runtimeVariables.standSingleton())
			return null;
		
		logger.info("Tell ATM to direct finish run befor execution.");
		if (null == this.atmManagerAPI)
			atmManagerAPI = serverApi.createAtmService(AtmManagerAPI.class, runtimeVariables.getAuthToken());
		
		Call<MeowlomoResponse> call = atmManagerAPI.finishRuns(run);
		Response<MeowlomoResponse> response;
		MeowlomoResponse reponseBody;
		
		try {
			response = call.execute();
			reponseBody = response.body();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		if (response.isSuccessful()) {
			runtimeVariables.setLastCommunicationTime(new Date());
			JsonNode responseJson = reponseBody.getMetadata();
			if (null != responseJson) {
				return responseJson.asText();
			} else {
				return null;
			}
		} else {
			logger.error("directFinishRun::error response from the server." + reponseBody.getError());
			return null;
		}
	}
}
