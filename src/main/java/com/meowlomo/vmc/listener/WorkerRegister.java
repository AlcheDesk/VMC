package com.meowlomo.vmc.listener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowlomo.vmc.config.RetrofitApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.core.SystemProperties;
import com.meowlomo.vmc.event.RegisterEvent;
import com.meowlomo.vmc.model.MeowlomoResponse;
import com.meowlomo.vmc.model.Worker;
import com.meowlomo.vmc.retrofit.EmsManagerAPI;

import retrofit2.Call;
import retrofit2.Response;

@Component
public class WorkerRegister implements ApplicationListener<RegisterEvent>{

	private static final Logger logger = LoggerFactory.getLogger(WorkerRegister.class);

	@Autowired
	RetrofitApi serverApi = new RetrofitApi();
	
	@Autowired
	RuntimeVariables runtimeVariables;
	
	@Autowired
	SystemProperties systemProperties;
	
//	@EventListener(ContextRefreshedEvent.class)
	
	public boolean registerWorkerToManager(){
		if (runtimeVariables.standSingleton()) {
			systemProperties.initSystemProperties();
			return true;
		}
			
		//keep trying to register to server
		logger.info("registering worker to manager ["+runtimeVariables.getEmsUrl()+"]");
		for(int retryCount = 0;retryCount < runtimeVariables.getRegistryRetry() && !runtimeVariables.isRegisted(); retryCount++){
			try {
				InetAddress.getByName(runtimeVariables.getEmsUrl());
			} catch (UnknownHostException ex) {
				logger.error("can not resolve manager's hostname,as it is {}",  runtimeVariables.getEmsUrl());
			}
			Worker worker = systemProperties.getSystemPropertiesForRegistration();
			System.err.println("Worker generate end.");
			logger.info("registering worker to manager [" + runtimeVariables.getEmsUrl() + "] Using IP:" + SystemProperties.ipAddress);
			Call<MeowlomoResponse> call = serverApi.createEmsService(EmsManagerAPI.class,runtimeVariables.getAuthToken()).registerWorkerToManager(new Worker[]{worker});
			Response<MeowlomoResponse> response = null;
			try {
				response = call.execute();
			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
			
			if (response.isSuccessful()){
				MeowlomoResponse reponseBody = response.body();
				//update the last successfully
				runtimeVariables.setLastCommunicationTime(new Date());
				//check the response
				
				//get the worker back
				ObjectMapper mapper = new ObjectMapper();
				List<Worker> workers = mapper.convertValue(reponseBody.getData(), new TypeReference<List<Worker>>(){});
				if(!workers.isEmpty()){
					logger.info("Worker Registed");
					//check the status is FREE on server site
					runtimeVariables.setRegisted(true);
					runtimeVariables.setUuid(worker.getUuid());
					//check the token and see if it is there
					Worker currentInfoFromManager = workers.get(0);
					UUID token = currentInfoFromManager.getToken();
					if (token == null) {
					    logger.error("Can not register the worker to the manager due to token error.");
		                return false;
					}
					else{
					    RuntimeVariables.token = token;
					    logger.info("======Server Response======\n" + response.toString());
	                    return true;
					}
				}else{
					logger.error("Can not register the worker to the manager.");
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public void onApplicationEvent(RegisterEvent event) {
		registerWorkerToManager();
	}
}
