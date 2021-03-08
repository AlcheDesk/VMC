package com.meowlomo.vmc.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.core.ComputerIdentifier;
import com.meowlomo.vmc.event.VMCEventPublisher;
import com.meowlomo.vmc.model.Task;
//import com.meowlomo.vmc.scheduler.MultiThreadTaskProcessor;
import com.meowlomo.vmc.util.JWTUtils;

@Component
public class RuntimeVariables {

	private volatile Logger logger = LoggerFactory.getLogger(RuntimeVariables.class);

	private boolean fileCleanPaused = false;
	
	public static volatile boolean HOLD_ON_WORKING_MODE_FOR_REBOOT = false;
	public static UUID token;

	private Boolean osgiReady = Boolean.FALSE;
	
	@Autowired
	JWTUtils jwtUtil;

	@Autowired
	public VMCEventPublisher publisher;
	// authentication
	private String authToken;
	
	@Value("${meowlomo.security.jwt.key}")
	private String key;
	
	@Value("${meowlomo.runtime.config.singleton}")
	private boolean standSingleton;
	
	@Value("${meowlomo.runtime.config.logFile}")
	private boolean logFileToServer;
	
	@Value("${meowlomo.runtime.config.writetask}")
	private boolean writeTask;
	
	@Value("${meowlomo.runtime.config.closesystemout}")
	private boolean closeSystemOut;
	
	@Value("${meowlomo.runtime.config.closesystemerr}")
	private boolean closeSystemErr;
	
	@Value("${meowlomo.runtime.config.refuseAnonymousEMS}")
	private boolean refuseAnonymousEMS;
	
	@Value("${meowlomo.config.vmc.firefox.version}")
	private String firefoxVersion;
	
	@Value("${meowlomo.config.vmc.chrome.version}")
	private String chromeVersion;
	
	@Value("${meowlomo.config.vmc.ie.version}")
	private String ieVersion;
	
	@Value("${meowlomo.config.vmc.edge.version}")
	private String edgeVersion;
	
	@Autowired
	public LangMgr langMgr;
	
	@Autowired
	public BundleInfo bundleMap;
	
	private String webdriverName = "IWebDriver";
	private String webdriverTestName = "doTestProcess";
	private String webdriverEndName = "notifyTimeout";
	
	@Value("${meowlomo.config.service.file.type}")
    private String serviceType;
    @Value("${meowlomo.config.service.file.path}")
    private String path;
    @Value("${meowlomo.config.service.file.username}")
    private String username;
    @Value("${meowlomo.config.service.file.password}")
    private String password;
    @Value("${meowlomo.config.service.file.hostname}")
    private String hostname;
    @Value("${meowlomo.config.service.file.domain}")
    private String serverDomain;
    @Value("${meowlomo.config.service.file.sharename}")
    private String sharename;
    
	// worker status settings
	private boolean registed = false;
	private String status = "DOWN";
	private UUID uuid = null;
	private Date lastCommunicationTime = null;
	private boolean reboot = false;
	private boolean update = false;
	private boolean restart = false;

	private boolean inGUIProcessing = false;
	
	

	/*
	 * tasks data CopyOnWriteArrayList is costly, need to find a better
	 * alternative to this object type.
	 * 
	 */
	private LinkedBlockingQueue<Task> tasksQueue = new LinkedBlockingQueue<Task>();
	private ConcurrentHashMap<String, Task> taskSet = new ConcurrentHashMap<String, Task>(100);
	private ConcurrentHashMap<String, Boolean> nonInteractiveWorkingFlag = new ConcurrentHashMap<String, Boolean>(100);

	private Set<String> statuses = new HashSet<String>();
	private Set<String> jobTypes = new HashSet<String>();
	private Set<String> taskTypes = new HashSet<String>();
	private Set<String> operatingSystems = new HashSet<String>();
	private Set<String> groups = new HashSet<String>();

	private Integer daysKeepTemp = null;
	private int liveCheckerInterval = 30;
	private boolean rebootOnTimeout = false;
	
	//TODO
	@Value("${vmc.config.log.localvmclog}")
	private String fileServerMountPoint;
	
	private long fileServerRequiredSpace = 104857600;
	private int registryRetry = 5;

	// manager settings
	@Value("${vmc.config.retrofit.ems.baseUrl}")
	private String emsUrl;
	
	@Value("${vmc.config.retrofit.atm.baseUrl}")
	private String atmUrl;


	//package level
	public Boolean bNewDataBase = false;
	public String dbUrl = "";
	public String dbClassName = "";
	public String dbUser = "";
	public String dbPwd = "";
	public String dbName = "";
	
	public boolean isFileCleanPaused() {
		return fileCleanPaused;
	}

	public void setFileCleanPaused(boolean fileCleanPaused) {
		this.fileCleanPaused = fileCleanPaused;
	}

	public boolean isRegisted() {
		return registed;
	}

	public void setRegisted(boolean registed) {
		this.registed = registed;
	}

	public String getStatus() {
		return status;
	}
	
	public boolean freeStatus() {
		return this.status.equalsIgnoreCase(Constant.STATUS_FREE);
	}
	
	public boolean workingStatus() {
		return this.status.equalsIgnoreCase(Constant.STATUS_WORKING);
	}

	protected void setStatus(String status) {
		this.status = status;
	}
	
	public void setFreeStatus() {
		setStatus(Constant.STATUS_FREE);
	}
	
	public void setWorkingStatus() {
		setStatus(Constant.STATUS_WORKING);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Date getLastCommunicationTime() {
		return lastCommunicationTime;
	}

	public void setLastCommunicationTime(Date lastCommunicationTime) {
		this.lastCommunicationTime = lastCommunicationTime;
	}

	public boolean isReboot() {
		return reboot;
	}

	public void setReboot(boolean reboot) {
		this.reboot = reboot;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public LinkedBlockingQueue<Task> getTasksQueue() {
		return tasksQueue;
	}

	public void setTasksQueue(LinkedBlockingQueue<Task> tasksQueue) {
		this.tasksQueue = tasksQueue;
	}

	public Set<String> getJobTypes() {
		return jobTypes;
	}

	public void setJobTypes(Set<String> jobTypes) {
		this.jobTypes = jobTypes;
	}

	public Set<String> getTaskTypes() {
		return taskTypes;
	}

	public void setTaskTypes(Set<String> taskTypes) {
		this.taskTypes = taskTypes;
	}

	public Set<String> getOperatingSystems() {
		return operatingSystems;
	}

	public void setOperatingSystems(Set<String> operatingSystems) {
		this.operatingSystems = operatingSystems;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public void setGroups(Set<String> groups) {
		this.groups = groups;
	}
	
//	public Map<String, String> getElementTypeToDriverType() {
//		return elementTypeToDriverType;
//	}
//	
//	public void setElementTypeToDriverType(Map<String, String> elementTypeToDriverType){
//		this.elementTypeToDriverType = elementTypeToDriverType;
//	}

	public boolean isRebootOnTimeout() {
		return rebootOnTimeout;
	}

	public void setRebootOnTimeout(boolean rebootOnTimeout) {
		this.rebootOnTimeout = rebootOnTimeout;
	}

	public String getFileServerMountPoint() {
		return fileServerMountPoint;
	}

	public void setFileServerMountPoint(String fileServerMountPoint) {
		this.fileServerMountPoint = fileServerMountPoint;
	}

	public long getFileServerRequiredSpace() {
		return fileServerRequiredSpace;
	}

	public void setFileServerRequiredSpace(long fileServerRequiredSpace) {
		this.fileServerRequiredSpace = fileServerRequiredSpace;
	}

	public int getRegistryRetry() {
		return registryRetry;
	}

	@Value("${vmc.config.registery.retry}")
	public void setRegistryRetry(int registryRetry) {
		this.registryRetry = registryRetry;
	}

	public ObjectNode genReportBackParamsters(ObjectNode dataContent, String runType, long runId){
		ObjectNode parameters = JsonNodeFactory.instance.objectNode();
		ObjectNode addStepLog = JsonNodeFactory.instance.objectNode();
		addStepLog.put("url", atmUrl + "api/instructionResults/{instructionResultId}/stepLogs?mode=" + runType);
		addStepLog.put("method", "post");
		
		ObjectNode addExecutionLog = JsonNodeFactory.instance.objectNode();
		addExecutionLog.put("url", atmUrl + "api/executionLogs?mode=" + runType);
		addExecutionLog.put("method", "post");
		
		ObjectNode addInstructionResult = JsonNodeFactory.instance.objectNode();
		addInstructionResult.put("url", atmUrl + "api/runs/" + runId + "/instructionResults?mode=" + runType);//{runId}
		addInstructionResult.put("method", "post");
		
		ObjectNode updateInstructionResult = JsonNodeFactory.instance.objectNode();
		updateInstructionResult.put("url", atmUrl + "api/runs/" + runId + "/instructionResults?mode=" + runType);//{runId}
		updateInstructionResult.put("method", "patch");
		
		ObjectNode updateRun = JsonNodeFactory.instance.objectNode();
		updateRun.put("url", atmUrl + "api/runs");
		updateRun.put("method", "patch");
		ObjectNode content = JsonNodeFactory.instance.objectNode();
		content.put("id", runId);
		content.put("firefox", firefoxVersion);
		content.put("chrome", chromeVersion);
		content.put("ie", ieVersion);
		content.put("edge", edgeVersion);
		updateRun.set("content", content);
		
		ObjectNode addRun = JsonNodeFactory.instance.objectNode();
		addRun.put("url", atmUrl + "api/testCases/{testCaseId}/runs?mode=" + runType);
		addRun.put("method", "post");
		
		content = JsonNodeFactory.instance.objectNode();
		content.put("finished", true);
		
		ObjectNode finishRun = JsonNodeFactory.instance.objectNode();
		finishRun.put("url", atmUrl + "api/runs?mode=" + runType);	///{runId}
		finishRun.put("method", "patch");
		finishRun.set("content", content);
		
		ObjectNode finishInstructionResult = JsonNodeFactory.instance.objectNode();
		finishInstructionResult.put("url", atmUrl + "api/instructionResults?mode=" + runType);///{instructionResultId}
		finishInstructionResult.put("method", "patch");
		finishInstructionResult.set("content", content);
		
		ObjectNode fileServiceParameters = genFileServerParameters(dataContent);
		
		ObjectNode addStepFileLog = JsonNodeFactory.instance.objectNode();
		addStepFileLog.put("url", atmUrl + "api/files?mode=" + runType);
		addStepFileLog.put("method", "post");
		
		parameters.set("addExecutionStepLog",		addExecutionLog);
		parameters.set("addStepLog", 				addStepLog);
		parameters.set("addInstructionResult", 		addInstructionResult);
		parameters.set("updateInstructionResult", 	updateInstructionResult);
		parameters.set("addRun", 					addRun);
		parameters.set("finishRun", 				finishRun);
		parameters.set("finishInstructionResult", 	finishInstructionResult);
		if (null != fileServiceParameters)
			parameters.set("remoteFileServer", 		fileServiceParameters);
		parameters.set("addStepFileLog",			addStepFileLog);
		parameters.set("updateRun", updateRun);
		
		return parameters;
	}

	private ObjectNode genFileServerParametersDirectly() {
		ObjectNode fileServiceParameters = JsonNodeFactory.instance.objectNode();
		fileServiceParameters.put("hostname", hostname);
		fileServiceParameters.put("username", username);
		fileServiceParameters.put("password", password);
		fileServiceParameters.put("domain", serverDomain);
		fileServiceParameters.put("sharename", sharename);
		fileServiceParameters.put("path", path);
		return fileServiceParameters;
	}
	
	private ObjectNode genFileServerParameters(ObjectNode dataContent) {
		ObjectNode fileServiceParameters = JsonNodeFactory.instance.objectNode();
		ArrayNode storages = dataContent.withArray("storages");
		if (null == storages || (storages.isArray() && 0 == storages.size())){
			return genFileServerParametersDirectly();
		}
		JsonNode defaultStorage = null;
		for(JsonNode storage : storages){
			defaultStorage = storage;
			if (defaultStorage.get("name").asText().contains("Default")){
				break;
			}
		}
		if (null == defaultStorage){
			return null;
		}else{
			if (defaultStorage instanceof JsonNode && defaultStorage.has("parameter") && defaultStorage.get("parameter").has("type")){
				if (defaultStorage.get("parameter").get("type").asText().equalsIgnoreCase("samba")){
					JsonNode params = defaultStorage.get("parameter");
					fileServiceParameters.put("hostname", defaultStorage.get("url").asText());
					fileServiceParameters.put("username", params.get("username").asText());
					fileServiceParameters.put("password", params.get("password").asText());
					if (null == params.get("domain"))
						fileServiceParameters.set("domain", JsonNodeFactory.instance.nullNode());
					else
						fileServiceParameters.put("domain", params.get("domain").asText());
					fileServiceParameters.put("sharename", params.get("shareName").asText());
					fileServiceParameters.put("path", params.has("path") ? params.get("path").asText() : (params.has("subPath") ? params.get("subPath").asText() : ""));
					return fileServiceParameters;
				}
			}
			return null;
		}
	}
	
	public String getAtmUrl() {
		return atmUrl;
	}
	
	public String getEmsUrl() {
		return emsUrl;
	}
	
//	no use, no supply 
//	@Value("${vmc.config.retrofit.ems.baseUrl}")
//	public void setEmsUrl(String emsUrl) {
//		this.emsUrl = emsUrl;
//	}
//	
//	@Value("${vmc.config.retrofit.atm.baseUrl}")
//	public void setAtmUrl(String atmUrl) {
//		this.atmUrl = atmUrl;
//	}

	public Set<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(Set<String> statuses) {
		this.statuses = statuses;
	}

	public String getAuthToken() {
		if (authToken == null) {
			this.authToken = jwtUtil.generateJWS(ComputerIdentifier.generateLicenseKey().toString());
		}
		return authToken;
	}

	public void setAuthToken(String authToken) {
		logger.info("Token is set =>" + authToken);
		this.authToken = authToken;
	}

	public Integer getDaysKeepTemp() {
		return daysKeepTemp;
	}

	public void setDaysKeepTemp(Integer daysKeepTemp) {
		this.daysKeepTemp = daysKeepTemp;
	}

	public int getLiveCheckerInterval() {
		return liveCheckerInterval;
	}

	public void setLiveCheckerInterval(int liveCheckerInterval) {
		this.liveCheckerInterval = liveCheckerInterval;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	// ========================================
	public synchronized void addTaskToList(Task task) {
		logger.info("Queue task [" + task.getUuid() + "] to the queue.");
		this.tasksQueue.add(task);
	}
	
	public synchronized Task getTask() {
		Task task = this.tasksQueue.poll();
		if (null != task)
			logger.info("Poll task [" + task.getUuid() + "] from the queue.");
		return task;
	}

	public synchronized boolean removeTask(Task task) {
		// remove the take from the task queue
		return this.tasksQueue.remove(task);
	}
	
	public synchronized void registerTaskBeginProcess(Task task){
		if (!taskSet.containsKey(task.getUuid().toString())){
			taskSet.put(task.getUuid().toString(), task);
		} else {
			//TODO why here?
			System.err.println("[FATAL ERROR] one task emitted twice.");
		}
	}
	
	public synchronized void unRegisterTaskEndingProcess(Task task){
		taskSet.remove(task.getUuid().toString());
	}
	
	public synchronized ArrayList<Task> getAllTasks() {
		ArrayList<Task> allTasks = new ArrayList<Task>();
		for(ConcurrentHashMap.Entry<String, Task> entry : taskSet.entrySet()){
			allTasks.add(entry.getValue());
		}
		return allTasks;
	}

	public synchronized boolean isTaskQueueEmpty() {
		return this.tasksQueue.isEmpty();
	}

	public synchronized void updateWorkerStatusSpringBootWay() {
		setWorkingStatus();
		if (!isWorking() && !inGUIProcessing)
			setFreeStatus();
	}

	public synchronized void updateNonTaskProcessingStatus(Task task) {
		String key = getTaskKey(task);
		setWorkingStatus();
		if (!nonInteractiveWorkingFlag.contains(key)) {
			nonInteractiveWorkingFlag.put(key, Boolean.TRUE);
		}
	}

	public String getTaskKey(Task task) {
		String taskUUID = task.getUuid().toString();
		String taskName = task.getName();
		if (null == taskName)
			taskName = "";
		String taskKey = String.format("Key_%s_%s", taskUUID, taskName);
		return taskKey;
	}

	public boolean rejectGUI(){
		return inGUIProcessing() || isWorking();
	}
	
	public boolean inGUIProcessing() {
		return inGUIProcessing;
	}

	public void setInGUIProcessing(boolean handlingGUI) {
		this.inGUIProcessing = handlingGUI;
	}

	// TODO 线程访问的一致性与安全性 待测
	public Boolean isWorking() {
		if (!osgiReady) return Boolean.TRUE;
		for (Entry<String, Boolean> entry : nonInteractiveWorkingFlag.entrySet()) {
			Boolean bWorking = entry.getValue();
			if (bWorking)
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean logFileToServer(){
		return logFileToServer;
	}
	
	public boolean standSingleton(){
		return standSingleton;
	}
	
	public boolean closeSystemOut(){
		return closeSystemOut;
	}
	
	public boolean closeSystemErr(){
		return closeSystemErr;
	}
	
	public boolean refuseAnonymousEMS(){
		return refuseAnonymousEMS;
	}
	
	public boolean writeTask(){
		return writeTask;
	}
	
	public String webdriverPackageName(){
		return webdriverName;
	}
	
	public String webdriverTestName(){
		return webdriverTestName;
	}
	
	public String webdriverStopName(){
		return webdriverEndName;
	}
	
	public Boolean setOSGiReady() {
		setFreeStatus();
		return osgiReady = Boolean.TRUE;
	}
}
