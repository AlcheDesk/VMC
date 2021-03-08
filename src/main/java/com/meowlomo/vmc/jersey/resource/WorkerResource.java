package com.meowlomo.vmc.jersey.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.config.Constant;
import com.meowlomo.vmc.config.RetrofitApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.core.ManagerRequestProcessor;
import com.meowlomo.vmc.core.SystemProperties;
import com.meowlomo.vmc.jersey.exception.CustomBadRequestException;
import com.meowlomo.vmc.jersey.exception.CustomForbiddenException;
import com.meowlomo.vmc.jersey.exception.CustomInternalServerErrorException;
import com.meowlomo.vmc.jersey.exception.CustomNotAcceptableException;
import com.meowlomo.vmc.jersey.exception.CustomNotAllowedException;
import com.meowlomo.vmc.jersey.exception.CustomNotAuthorizedException;
import com.meowlomo.vmc.jersey.exception.CustomNotFoundException;
import com.meowlomo.vmc.jersey.exception.CustomNotSupportedException;
import com.meowlomo.vmc.jersey.exception.CustomServiceUnavailableException;
import com.meowlomo.vmc.model.MeowlomoResponse;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.model.Worker;
import com.meowlomo.vmc.osgi.OSGiUtil;
import com.meowlomo.vmc.retrofit.EmsManagerAPI;
import com.meowlomo.vmc.scheduler.AgentHealthChecker;
import com.meowlomo.vmc.service.WorkerUtilService;
import com.meowlomo.vmc.task.DubboGenericServiceCallParam;
import com.meowlomo.vmc.util.PlatformUtil;
import com.meowlomo.vmc.util.RestartRebootUtils;
import com.meowlomo.vmc.util.SQLiteConnect;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Component
@Path("/")
public class WorkerResource {

    static final Logger logger = LoggerFactory.getLogger(WorkerResource.class);
    
    @Value("${meowlomo.vmc.worker.reboot-delay:5}")
    private int rebootDelay;

    @Autowired
    RetrofitApi serverApi = new RetrofitApi();

    @Autowired
    AgentHealthChecker agentHealthChecker;

    @Autowired
    RuntimeVariables runtimeVariables;
    
    @Autowired
    private WorkerUtilService workerUtilService;

//    @Autowired
//    private JWTUtils jwtUtil;

    private static final String ERROR_TYPE = "VMC"; 

    @Context
    UriInfo uriInfo;

    @Context
    SecurityContext securityContext;

    @Context
    HttpServletRequest httpRequest;

    //For dubbo generic service
    static ApplicationConfig application;
    static RegistryConfig registry;
    static {
    	application = new ApplicationConfig();
        application.setName("Meowlomo-test-consumer");
        registry = new RegistryConfig();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public String responseInfo() {
        return "You can use the following URLs to comunicate \n"
                + "/task \n"
                + "/status \n"
                + "/stop \n"
                + "/reboot \n";
    }
   
    @POST
    @Path("generic")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Do dubbo's generic call fro  查询执行单元情况及属性", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse doDubboGenericCallForOSGi(@HeaderParam("Token") String token, DubboGenericServiceCallParam genericServiceParam){
    	abortIfNotSelf(getIPInfo(), "dubbo generic call request");
        try {
            if (!workerUtilService.checkToken(token)) {
        		return invalidToken();
            }
            String zkAddress = String.format("zookeeper://%s:%s", genericServiceParam.zkHost, genericServiceParam.zkPort);
            ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
            reference.setApplication(application);
            registry.setAddress(zkAddress);
            reference.setRegistry(registry);
            reference.setInterface(genericServiceParam.interfaceClass);
            reference.setGeneric(true);
            if (null != genericServiceParam.interfaceVersion && !genericServiceParam.interfaceVersion.isEmpty())
            	reference.setVersion(genericServiceParam.interfaceVersion);
            ReferenceConfigCache cache = ReferenceConfigCache.getCache();
            GenericService genericService = cache.get(reference);
            
            int paramLen = genericServiceParam.parameter.size();
            String[] invokeParamTyeps = new String[paramLen];
            Object[] invokeParams = new Object[paramLen];
            for(int i = 0; i < paramLen; i++){
                invokeParamTyeps[i] = genericServiceParam.parameter.get(i).get("KeyType").asText() + "";
                invokeParams[i] = genericServiceParam.parameter.get(i).get("KeyValue").asText();
            }
            Object dubboResult = genericService.$invoke(genericServiceParam.methodName, invokeParamTyeps, invokeParams);
            
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();
            metadata.put("status", "OK");
            MeowlomoResponse response = new MeowlomoResponse(metadata, dubboResult, null);
			logger.info("Response with data " + response.toString());
            return response;
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01B";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Access for status and properties. Version 1 - (version in URL) 查询执行单元情况及属性", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse foremanAskStatus(@HeaderParam("Token") String token){
    	abortIfConfigged(getIPInfo(), "status check");
    	
        try {
            if (!workerUtilService.checkToken(token)) {
        		return invalidToken();
            }
        	
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();
            //put status
            metadata.put("status", runtimeVariables.getStatus());
            MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
            logger.info("Response with data "+response.toString());
            return response;
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01A";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }

    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Access for curretnly health status. Version 1 - (version in URL) 查询执行单元健康情况", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse managerAskForHealthCheck(@HeaderParam("Token") String token){
        abortIfConfigged(getIPInfo(), "health check");
        try {
            if (!workerUtilService.checkToken(token)) {
        		return invalidToken();
            }
        	
            //get the current status
            boolean fileMountingpointCheck = agentHealthChecker.isFileSystemOK();
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();
            //put file mounting point check
            metadata.put("fileSystemOK", fileMountingpointCheck);
            //put status
            metadata.put("status", runtimeVariables.getStatus());
            metadata.put("token", RuntimeVariables.token == null ? null : RuntimeVariables.token.toString());
            MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
            logger.info("Response with data "+response.toString());
            return response;
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01B";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }

	private String getIPInfo() {
		String clientIP = httpRequest.getHeader("X-FORWARDED-FOR");  
		if (clientIP == null) {
			clientIP = httpRequest.getRemoteAddr();  
		}
		String clientIPInfo = " IP is " + ((null == clientIP || clientIP.isEmpty()) ? "null" : clientIP);
		return clientIPInfo;
	}

    @GET
    @Path("tasks")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Access for curretnly running task. Version 1 - (version in URL) 用于访问正在执行的单元", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse foremanAskForTask(@HeaderParam("Token") String token){
    	abortIfConfigged(getIPInfo(), "tasks check");
        try {
            if (!workerUtilService.checkToken(token)) {
        		return invalidToken();
            }
        	
            logger.info("receive get task status request from server.");
            ArrayList<Task> responseJson = runtimeVariables.getAllTasks();
            logger.info("Response with data "+responseJson.toString());
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();
            MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
            logger.info("Response with data "+response.toString());
            return response;
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01C";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }
  
    /**
     * TODO
     * Debug时使用
     */
    @GET
    @Path("dojob")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "[Debug Test]For Task assignment. Version 1 - (version in URL) 用于Debug时分配执行", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse doLocalJob(){
        logger.info("[Debug Test] received task assignment request from the server");
        try {
        	File jsonTask = null;
        	if (PlatformUtil.windows()) {
        		jsonTask = new File("C:\\vmctask\\commandToBundleDirectTask.txt");
        	} else if (PlatformUtil.linux()) {
        		jsonTask = new File("commandToBundleDirectTask.txt");
        	}
        	if (null != jsonTask && !jsonTask.exists()){
        		jsonTask = new File("commandToBundleDirectTask.txt");
        		if (!jsonTask.exists()){
        			MeowlomoResponse response = new MeowlomoResponse(null, runtimeVariables.getAllTasks(), null);
	                logger.info("[No task] dispatched. Response with data " + response.toString());
	                return response;
        		}
        	}
        	InputStream is = new FileInputStream(jsonTask);
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                // stringBuilder.append(line);
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
            String jsonString = String.valueOf(stringBuilder);
        	
        	ObjectMapper mapper = new ObjectMapper();
            Task task = (Task)mapper.readValue(jsonString, Task.class);

        	task.setInteractive(true);
        	ObjectNode metadata = JsonNodeFactory.instance.objectNode();
        	metadata.put("workerStatus", Constant.STATUS_WORKING);
        	
        	if ((runtimeVariables.rejectGUI() || runtimeVariables.workingStatus()) && task.getInteractive()){
        		metadata.put("reject", true);
        		logger.info("JSON response [reject] in WorkerResource::foremanAssignTask for VMC is working. \n"+task.toString());
        	} else {
	            runtimeVariables.setWorkingStatus();
	            //executed by schedule
	            runtimeVariables.addTaskToList(task);
	            logger.info("JSON response in WorkerResource::foremanAssignTask \n"+task.toString());
        	}
            MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
            logger.info("Response with data "+response.toString());
            return response;
            
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01D";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }
    
    @POST
    @Path("tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "For Task assignment. Version 1 - (version in URL) 用于分配执行", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "POST")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse foremanAssignTask(@HeaderParam("Token") String token, Task task){
        abortIfConfigged(getIPInfo(), "task assignment request");
        try {

        	if (runtimeVariables.writeTask()){
        		ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(task);
        		try{
        			if (PlatformUtil.windows()) {
        				SQLiteConnect.cacheTaskAtLocal(task);
			        	File outFile = new File("C:\\vmctask\\commandToBundleDirectTask.txt");
						OutputStream os = new FileOutputStream(outFile);
						os.write(jsonString.getBytes());
						os.close();
        			} else if (PlatformUtil.linux()) {
        				File outFile = new File("commandToBundleDirectTask.txt");
						OutputStream os = new FileOutputStream(outFile);
						os.write(jsonString.getBytes());
						os.close();
        			}
        		}
        		catch(Exception e){
    	        	logger.error("[First Try] Write Direct Task Data in Json format fail.");
    				try{
    					File outFile = new File("commandToBundleDirectTask.txt");
    					OutputStream os = new FileOutputStream(outFile);
    					os.write(jsonString.getBytes());
    					os.close();
    				}catch(Exception e1){
    					logger.error("[Second Try] Write Direct Task Data in Json format fail.");
    				}	
        		}
        	}

        	//TODO why
        	task.setInteractive(true);
        	
            if (!workerUtilService.checkToken(token)) {
            	return invalidToken();
            }
        	
        	ObjectNode metadata = JsonNodeFactory.instance.objectNode();
        	metadata.put("workerStatus", Constant.STATUS_WORKING);
        	
        	//TODO throw the task if working
        	if ((runtimeVariables.rejectGUI() || runtimeVariables.workingStatus()) && task.getInteractive()){
        		metadata.put("reject", true);
        		logger.info("JSON response [reject] in WorkerResource::foremanAssignTask for VMC is working. \n"+task.toString());
        	} else if (runtimeVariables.isReboot()) {
        		metadata.put("reject", true);
        		logger.info("JSON response [reject] in WorkerResource::foremanAssignTask for VMC is preparing reboot. \n"+task.toString());
        	} else {
	            runtimeVariables.setWorkingStatus();
	            runtimeVariables.addTaskToList(task);
	            logger.info("JSON response in WorkerResource::foremanAssignTask \n"+task.toString());
        	}
            MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
            logger.info("Response with data " + response.toString());
            return response;
            
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01E";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }

    @GET
    @Path("restart/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "For restarting the VMC exectuion unit. Version 1 - (version in URL) 重启执行单元", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse foremanAskToRestart(@HeaderParam("Token") String token){
        //set the restart the flag to be true
        // the program will restart itself after finishing the current task
    	abortIfConfigged(getIPInfo(), "restart request");
        try {
            if (!workerUtilService.checkToken(token)) {
        		return invalidToken();
            }
        	
            runtimeVariables.setRestart(true);
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();
            metadata.put("restart", runtimeVariables.isRestart());
            MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
            logger.info("Response with data "+response.toString());
            return response;
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01F";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }

    @GET
    @Path("reboot")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "For restarting the VMC exectuion unit. Version 1 - (version in URL) 重启执行单元", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse foremanAskToReboot(@HeaderParam("Token") String token){
        //set the restart the flag to be true
        // the program will reboot the system after finishing the current Task
        abortIfConfigged(getIPInfo(), "reboot request");
        runtimeVariables.setReboot(true);
        ObjectNode metadata = JsonNodeFactory.instance.objectNode();
        try {
            if (!workerUtilService.checkToken(token)) {
        		return invalidToken();
            }
        	
            if(runtimeVariables.getAllTasks().size() != 0){
                logger.info("Worker still have running task. will change worker status to DOWN to prepare reboot.");
                metadata.put("success", "the worker will reboot after finishing the current task.");
            }else{
                if (!runtimeVariables.standSingleton() && runtimeVariables.isRegisted()) {
                    Worker sendWorker = new Worker();
                    sendWorker.setStatus("DOWN");
                    serverApi.createEmsService(EmsManagerAPI.class).updateWorkerToManager(runtimeVariables.getUuid().toString(), sendWorker);
                }
                try {
                    //RestartRebootUtils.rebootNow();
                	RestartRebootUtils.reboot(rebootDelay);
                    metadata.put("success", "the worker will reboot in 5 seconds.");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            return new MeowlomoResponse(metadata, null, null);
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01G";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }  	
    }
    
    @POST
    @Path("terminate")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "For restarting the VMC exectuion unit. Version 1 - (version in URL) 重启执行单元", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse foremanAskToTerminateTask(@HeaderParam("Token") String token, Task record){
        //set the restart the flag to be true
        // the program will reboot the system after finishing the current Task
        abortIfConfigged(getIPInfo(), "reboot request");
        runtimeVariables.setReboot(true);
        ObjectNode metadata = JsonNodeFactory.instance.objectNode();
        try {
            if (!workerUtilService.checkToken(token)) {
                return invalidToken();
            }
            
            //check and lock the worker for the termination
            boolean preperationResult = workerUtilService.prepareToKillCurrentTask(record);
            if(preperationResult){
                //the worker is prepared to terminate
                //we need to return and schedule a reboot
                RestartRebootUtils.reboot(rebootDelay);
                metadata.put("success", "the worker will reboot for terminating the current the task.");
            }else{
                //the worker can not be ready for termination
                UUID exuuid = UUID.randomUUID();
                String message = "Error on set worker to stand by for termination。Exception UUID ["+exuuid+"]。";
                String code = ERROR_TYPE + "01J";
                logger.error(message);
                throw new CustomServiceUnavailableException(null, message, null, code, exuuid);
                
            }
            return new MeowlomoResponse(metadata, null, null);
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
            String code = ERROR_TYPE + "01G";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }   
    }

    @POST
    @Path("manager")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "For executing request from EMS. Version 1 - (version in URL) 执行EMS的特殊指令", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "POST")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse managerRequest(@HeaderParam("Token") String token, JsonNode requestJson){
    	abortIfConfigged(getIPInfo(), "manager request");
        try {
        	//TODO runtimeVariables.getUuid().toString()
            if (!workerUtilService.checkToken(token)) {
                return invalidToken();
            }
        
            logger.info("received task assigment request fron the server with content "+ requestJson);
            ObjectNode jsonContent = (ObjectNode) requestJson;
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();
            if(requestJson == null){
                UUID exuuid = UUID.randomUUID();
                String developerMessage = "The Conetent is not valid json format。 error UUID="+exuuid+" couldn't execute patch."+" request body="+requestJson;
				String code = ERROR_TYPE + "01H";
                String message = "JSON格式不正确，请与管理员联系。并提供唯一码["+exuuid+"]";
                logger.error(developerMessage, httpRequest.getContextPath());
                throw new CustomBadRequestException(null, message, developerMessage, 0, code, exuuid);
            }else{
                boolean result = ManagerRequestProcessor.executeManagerRequest(jsonContent);
                metadata.put("executionResult", result);
                MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
                logger.info("Response with data "+response.toString());
                return response;
            }
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01J";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }
    
	/**
	 * { "bundleName":"com.meowlomo.ci.ems.bundle.webdriver",
	 * "bundlePath":"file:/d:/osgijar/pluginsMO/com.meowlomo.ci.ems.bundle.webdriver.jar",
	 * "showBundleNames":true }
	 * 
	 * @param token
	 * @param requestJson
	 * @return
	 */
    @POST
    @Path("reload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "For executing request from EMS. Version 1 - (version in URL) 执行EMS的特殊指令", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "POST")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse reloadRequest(@HeaderParam("Token") String token, JsonNode requestJson){
//    	abortIfConfigged(getIPInfo(), "manager request");
        try {
        	//TODO runtimeVariables.getUuid().toString()
            if (!workerUtilService.checkToken(token)) {
            	return invalidToken();
            }
        
            logger.info("received task assigment request fron the server with content "+ requestJson);
            ObjectNode jsonContent = (ObjectNode) requestJson;
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();
            if(requestJson == null){
                throw nullRequestJson();
            }else{
            	if (runtimeVariables.isWorking()) {
            		throw vmcBusyResponse();
            	}
                Object result = OSGiUtil.executeReloadRequest(jsonContent);
                if (null == result) result = "OK.";
                if (result instanceof String) {
                	if (((String)result).isEmpty())
                		result = "OK.";
                	metadata.put("executionResult", (String)result);
                } else if (result instanceof ArrayNode)
                	metadata.set("executionResult", (ArrayNode)result);
                MeowlomoResponse response = new MeowlomoResponse(metadata, runtimeVariables.getAllTasks(), null);
				logger.info("Response with data " + response.toString());
                return response;
            }
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01L";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }
    
    @GET
    @Path("tasks/{index}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取task的json", response = MeowlomoResponse.class, responseContainer = "List", httpMethod = "POST")
    @ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "NO MESSAGE"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码[\"+exuuid+\"]。", response = MeowlomoResponse.class) })
    public MeowlomoResponse getTask(@HeaderParam("Token") String token, @PathParam("index") Integer index){
        try {
            if (!workerUtilService.checkToken(token)) {
            	return invalidToken();
            }
        
            logger.info("To get task by desc order(start at 1):"+ index);
            ObjectNode metadata = JsonNodeFactory.instance.objectNode();

            Object result = SQLiteConnect.getByOrder(index);
            if (null == result) metadata.put("getTask", "Fail");
            else metadata.put("getTask", "OK");
            MeowlomoResponse response = new MeowlomoResponse(metadata, result, null);
			logger.info("Response with data " + response.toString());
            return response;
        } catch (CustomNotAuthorizedException|
                CustomBadRequestException|
                CustomForbiddenException|
                CustomNotAcceptableException|
                CustomNotAllowedException|
                CustomNotFoundException|
                CustomNotSupportedException|
                CustomServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            UUID exuuid = UUID.randomUUID();
            String message = "遇到系统内部错误 请与管理员联系。并提供错误唯一码["+exuuid+"]。";
			String code = ERROR_TYPE + "01L";
            logger.error(message,ex);
            throw new CustomInternalServerErrorException(ex, message, ex.getMessage(), code, exuuid);
        }
    }

	private CustomBadRequestException nullRequestJson() {
		UUID exuuid = UUID.randomUUID();
		String developerMessage = "The Conetent is not valid json format。 error UUID="+exuuid+" couldn't execute patch."+" request body=null";
		String code = ERROR_TYPE + "01K";
		String message = "JSON格式不正确，请与管理员联系。并提供唯一码[" + exuuid + "]";
		logger.error(developerMessage, httpRequest.getContextPath());
		return new CustomBadRequestException(null, message, developerMessage, 0, code, exuuid);
	}
	
	private CustomBadRequestException vmcBusyResponse() {
		UUID exuuid = UUID.randomUUID();
		String developerMessage = "The VMC is at working。 error UUID="+exuuid+" couldn't execute the request.";
		String code = ERROR_TYPE + "01N";
		String message = "VMC正忙，请与管理员联系。并提供唯一码[" + exuuid + "]";
		logger.error(developerMessage, httpRequest.getContextPath());
		return new CustomBadRequestException(null, message, developerMessage, 0, code, exuuid);
	}

	private MeowlomoResponse invalidToken() {
		ObjectNode metadata = JsonNodeFactory.instance.objectNode();
		//put status
		metadata.put("validToken", false);
		MeowlomoResponse response = new MeowlomoResponse(metadata, null, null);
		logger.info("Response with data "+response.toString());
		return response;
	}
    
//	protected Exception checkToken(String token) {
//		if (!jwtUtil.isTokenValid(token, ComputerIdentifier.generateLicenseKey().toString())) {
//			UUID exuuid = UUID.randomUUID();
//			String developerMessage = "exception UUID=" + exuuid + " couldn't execute insert, not authorized.";
//			String code = ERROR_TYPE + "01M";
//			String message = "无权进行操作。添加操作无法完成，请与管理员联系。并提供唯一码[" + exuuid + "]";
//			logger.error(developerMessage, httpRequest.getContextPath());
//			return new CustomNotAuthorizedException(null, message, developerMessage, code, exuuid);
//		}
//		return null;
//	}

	private void abortIfConfigged(String clientIPInfo, String apiInfo) {
		logger.info("Receive " + apiInfo + " from server." + clientIPInfo);
		
		//单独运行
		if (runtimeVariables.standSingleton()){
    		logger.info("[Reject] [Stand Singleton] " + apiInfo + " from server." + clientIPInfo);
    		throw new CustomBadRequestException(null, "Reject by Stand Singleton Property.",
    				"", HttpServletResponse.SC_FORBIDDEN, "VMC08B", UUID.randomUUID());
    	}
		
		//拒绝非配置ip的EMS的骚扰
    	if (runtimeVariables.refuseAnonymousEMS()){
    		try {
    			URI uri = new URI(runtimeVariables.getEmsUrl());
    			String host = uri.getHost();
    			if (null != host && !host.isEmpty() && !clientIPInfo.contains(host)){
    				logger.info("[Reject] [Refuse Anonymous EMS] " + apiInfo + " from." + clientIPInfo);
    				throw new CustomBadRequestException(null, "Reject by Refuse Anonymous EMS Property.",
    						"", HttpServletResponse.SC_FORBIDDEN, "VMC08B", UUID.randomUUID());
    			}
    		} catch (URISyntaxException e) {
    			//eat it
    		}
    	}
	}
	
	private void abortIfNotSelf(String clientIPInfo, String apiInfo) {
		logger.info("Receive " + apiInfo + " from server." + clientIPInfo);
		try {
			do {
				if (StringUtils.isEmpty(clientIPInfo) || StringUtils.isEmpty(SystemProperties.ipAddress)) break;
				if (runtimeVariables.standSingleton()) return;
				if (0 == clientIPInfo.compareTo("127.0.0.1")) return;
				if (0 == clientIPInfo.compareTo(SystemProperties.ipAddress)) return;
			} while(false);
			
			logger.info("[Reject] [Refuse Not Local or Loopback address] " + apiInfo + " from." + clientIPInfo);
			throw new CustomBadRequestException(null, "Reject by Refuse Anonymous EMS Property.", "",
					HttpServletResponse.SC_FORBIDDEN, "VMC08B", UUID.randomUUID());
		} catch (Exception e) {
			// eat it
		}
	}
}
