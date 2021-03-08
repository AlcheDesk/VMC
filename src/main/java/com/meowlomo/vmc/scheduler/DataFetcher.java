package com.meowlomo.vmc.scheduler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowlomo.vmc.config.RetrofitApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.model.Group;
import com.meowlomo.vmc.model.JobType;
import com.meowlomo.vmc.model.MeowlomoResponse;
import com.meowlomo.vmc.model.OperatingSystem;
import com.meowlomo.vmc.model.Status;
import com.meowlomo.vmc.model.TaskType;
import com.meowlomo.vmc.retrofit.EmsManagerAPI;

import retrofit2.Call;
import retrofit2.Response;

@Component
public class DataFetcher {

	static final Logger logger = LoggerFactory.getLogger(DataFetcher.class);

	@Autowired
	RetrofitApi serverApi;

	@Autowired
	RuntimeVariables runtimeVariables;

	/**
	 * Fetch status.
	 */
	@Scheduled(fixedRate = 300000)
	public void fetchStatus() {
		if (runtimeVariables.standSingleton()){
    		logger.info("fetching status. direct return by standSingleton !!!");
    		return;
    	}
		logger.info("fetching status");
		try {
			HashSet<String> names = new HashSet<String>();
			HashMap<String,Long> nameToIDMap = new HashMap<String,Long>();
			HashMap<Long,String> idToNameMap = new HashMap<Long,String>();
			Call<MeowlomoResponse> callResult = serverApi.createEmsService(EmsManagerAPI.class).getStatus();
			Response<MeowlomoResponse> response = callResult.execute();
			if(response.isSuccessful()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<Status> records = mapper.readValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.body().getData()), 
						new TypeReference<List<Status>>(){});
				if(!records.isEmpty()) {
					for(int count = 0; count < records.size(); count++){  
						Status record = records.get(count);
						nameToIDMap.put(record.getName(), record.getId());
						idToNameMap.put(record.getId(), record.getName());
						names.add(record.getName());
					}
					//put the map to the runtime variables
					runtimeVariables.setStatuses(names);
				}
			}
			else {

			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Fetch job type.
	 */
	@Scheduled(fixedRate = 300000)
	public void fetchJobType() {
		if (runtimeVariables.standSingleton()){
    		logger.info("fetching job type. direct return by standSingleton !!!");
    		return;
    	}
		logger.info("fetching job type");
		try {
			HashSet<String> names = new HashSet<String>();
			HashMap<String,Long> nameToIDMap = new HashMap<String,Long>();
			HashMap<Long,String> idToNameMap = new HashMap<Long,String>();
			Call<MeowlomoResponse> callResult = serverApi.createEmsService(EmsManagerAPI.class).getJobType();
			Response<MeowlomoResponse> response = callResult.execute();
			if(response.isSuccessful()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<JobType> records = mapper.readValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.body().getData()), new TypeReference<List<JobType>>(){});
				if(!records.isEmpty()) {
					for(int count = 0; count < records.size(); count++){  
						JobType record = records.get(count);
						nameToIDMap.put(record.getName(), record.getId());
						idToNameMap.put(record.getId(), record.getName());
						names.add(record.getName());
					}
					//put the map to the runtime variables
					runtimeVariables.setJobTypes(names);
				}
			}
			else {

			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Fetch task type.
	 */
	@Scheduled(fixedRate = 300000)
	public void fetchTaskType() {
		if (runtimeVariables.standSingleton()){
    		logger.info("fetching task type. direct return by standSingleton !!!");
    		return;
    	}
		logger.info("fetching task type");
		try {
			HashSet<String> names = new HashSet<String>();
			HashMap<String,Long> nameToIDMap = new HashMap<String,Long>();
			HashMap<Long,String> idToNameMap = new HashMap<Long,String>();
			Call<MeowlomoResponse> callResult = serverApi.createEmsService(EmsManagerAPI.class).getTaskType();
			Response<MeowlomoResponse> response = callResult.execute();
			if(response.isSuccessful()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<TaskType> records = mapper.readValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.body().getData()), new TypeReference<List<TaskType>>(){});
				if(!records.isEmpty()) {
					for(int count = 0; count < records.size(); count++){  
						TaskType record = records.get(count);
						nameToIDMap.put(record.getName(), record.getId());
						idToNameMap.put(record.getId(), record.getName());
						names.add(record.getName());
					}
					//put the map to the runtime variables
					runtimeVariables.setTaskTypes(names);
				}
			}
			else {

			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Fetch group.
	 */
	@Scheduled(fixedRate = 300000)
	public void fetchGroup() {
		if (runtimeVariables.standSingleton()){
    		logger.info("fetching group. direct return by standSingleton !!!");
    		return;
    	}
		logger.info("fetching group");
		try {
			HashSet<String> names = new HashSet<String>();
			HashMap<String,Long> nameToIDMap = new HashMap<String,Long>();
			HashMap<Long,String> idToNameMap = new HashMap<Long,String>();
			Call<MeowlomoResponse> callResult = serverApi.createEmsService(EmsManagerAPI.class).getGroup();
			Response<MeowlomoResponse> response = callResult.execute();
			if(response.isSuccessful()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<Group> records = mapper.readValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.body().getData()), new TypeReference<List<Group>>(){});
				if(!records.isEmpty()) {
					for(int count = 0; count < records.size(); count++){  
						Group record = records.get(count);
						nameToIDMap.put(record.getName(), record.getId());
						idToNameMap.put(record.getId(), record.getName());
						names.add(record.getName());
					}
					//put the map to the runtime variables
					runtimeVariables.setGroups(names);
				}
			}
			else {

			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Fetch operating system.
	 */
	@Scheduled(fixedRate = 3600000)
	public void fetchOperatingSystem() {
		if (runtimeVariables.standSingleton()){
    		logger.info("fetching operating syatem. direct return by standSingleton !!!");
    		return;
    	}
		
		logger.info("fetching operating syatem");
		try {
			HashSet<String> names = new HashSet<String>();
			HashMap<String,Long> nameToIDMap = new HashMap<String,Long>();
			HashMap<Long,String> idToNameMap = new HashMap<Long,String>();
			Call<MeowlomoResponse> callResult = serverApi.createEmsService(EmsManagerAPI.class).getGroup();
			Response<MeowlomoResponse> response = callResult.execute();
			if(response.isSuccessful()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<OperatingSystem> records = mapper.readValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.body().getData()), new TypeReference<List<OperatingSystem>>(){});
				if(!records.isEmpty()) {
					for(int count = 0; count < records.size(); count++){  
						OperatingSystem record = records.get(count);
						nameToIDMap.put(record.getName(), record.getId());
						idToNameMap.put(record.getId(), record.getName());
						names.add(record.getName());
					}
					//put the map to the runtime variables
					runtimeVariables.setOperatingSystems(names);
				}
			}
			else {

			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fetch element driver map
	 */
//	@Scheduled(fixedRate = 3600000)
//	public void fetchElementDriverMap() {
//		if (runtimeVariables.standSingleton()){
//    		logger.info("fetching element-driver map. direct return by standSingleton !!!");
//    		return;
//    	}
//		logger.info("fetching element-driver map");
//		try {
//			Map<String,String> elementToDriverMap = new HashMap<String,String>();
//			Map<String,String> driverToElementMap = new HashMap<String,String>();
//			Call<MeowlomoResponse> callResult = serverApi.createAtmService(AtmManagerAPI.class).getElementDriverMap();
//			Response<MeowlomoResponse> response = callResult.execute();
//			if(response.isSuccessful()) {
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				List<ObjectNode> records = mapper.readValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.body().getData()), new TypeReference<List<ObjectNode>>(){});
//				if(!records.isEmpty()) {
//					for(int count = 0; count < records.size(); count++){  
//						ObjectNode record = records.get(count);
//						Iterator<String> fieldIter = record.fieldNames();
//						while(fieldIter.hasNext()){
//							String elementType = fieldIter.next();
//							ArrayNode driverTypes = record.withArray(elementType);
//							
//							for(JsonNode driverTypeNode : driverTypes){
//								String driverType = driverTypeNode.asText();
//								elementToDriverMap.put(elementType, driverType);
//								driverToElementMap.put(driverType, elementType);
//							}
//						}
//					}
//					//put the map to the runtime variables
//					runtimeVariables.setElementTypeToDriverType(elementToDriverMap);
//				}
//			}
//			else {
//
//			}
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
