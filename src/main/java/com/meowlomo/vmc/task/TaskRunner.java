package com.meowlomo.vmc.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.core.ServerCommunicator;
import com.meowlomo.vmc.event.TaskHandler;
import com.meowlomo.vmc.exception.VMCJsonFieldNotExistException;
import com.meowlomo.vmc.exception.VMCJsonSchemaException;
import com.meowlomo.vmc.exception.VMCLogicalException;
import com.meowlomo.vmc.file.FileServerCommunicator;
import com.meowlomo.vmc.model.Run;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.osgi.OSGiUtil;
import com.meowlomo.vmc.util.CommunicationUtils;
import com.meowlomo.vmc.util.JsonNodeUtils;
import com.meowlomo.vmc.util.MOJsonUtil;
import com.meowlomo.vmc.util.MOLogger;
import com.meowlomo.vmc.util.MOObjectNode;
import com.meowlomo.vmc.util.PlatformUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class TaskRunnable.
 *
 * @author meteor
 */
//@Component
public class TaskRunner{
	
	List<String> functionTypes = Arrays.asList(new String[]{"webbrowser", "webfunction", "sql", "javascript"});
	List<String> apiTypes = Arrays.asList(new String[]{"rest_api"});
	List<String> expressionTypes = Arrays.asList(new String[]{"mathexpressionprocessor", "stringdataprocessor"});
	List<String> redisTypes = Arrays.asList(new String[]{"redis"});
	List<String> rpcDubboTypes = Arrays.asList(new String[]{"rpc_dubbo"});
	List<String> utilTypes = Arrays.asList(new String[]{"stringutil"});
	List<String> emailTypes = Arrays.asList(new String[]{"checkemail"});
	
	
	String[] functionInstructionKeys = {"logicalOrderIndex", "input", "target", "type", "testCaseId", "instructionOptions", "element", "action"};//, "elementAction"
	String[] apiInstructionKeys = {"logicalOrderIndex", "input", "data", "type", "element", "action"};//, "elementAction""action", 
	String[] expressionInstructionKeys = {"logicalOrderIndex", "input", "type"};
	String[] redisIntructionKeys = {"logicalOrderIndex", "target", "elementType", "element", "action", "target"};
	String[] rpcDubboInstructionKeys = {"logicalOrderIndex", "input", "data", "instructionOptions"};
	String[] utilInstructionKeys = {"logicalOrderIndex", "input", "type", "action"};
	String[] emailInstructionKeys = rpcDubboInstructionKeys;
	
	class InstructionTypeCnf {
		public int iType;
		public String[] keys;
		public InstructionTypeCnf(int t, String[] k) {
			iType = t;
			keys = k;
		}
	}
	//TODO 
	final static boolean refactor = true;
	
	public final static Integer DIRECTFAIL = new Integer(666);
	public final static Integer CHECKERROR = new Integer(60000);
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(TaskRunner.class);

	ServerCommunicator serverCommunicator;
	RuntimeVariables vars;
	FileServerCommunicator fileServerCommunicator;
	
	CommunicationUtils communicationUtils;

	/** The triggered. */
	//private properties.
	private boolean triggered = false;

	/** The task thread. */
	private Thread taskThread;

	/** The thread name. */
	private String threadName;

	/** The tries. */
	private int tryNumber = 0;

	/** The max try. */
	private Integer maxTry = null;

	/** The task ID. */
	private long taskID;
	
	private UUID newTaskID;

	/** The task UUID. */
	private UUID taskUUID;

	/** The json task. */
	private Task task;

	/** The launched time. */
	private LocalDateTime launchedTime;

	/** The process timeout. */
	private boolean processTimeout = false;

	/** The terminated. */
	private boolean terminated = false;

	/** The execution exit code. */
	private Integer executionExitCode = null;

	/** The execution status code. */
	private int     executionStatusCode = 2;

	/** The execution msg. */
	private String executionMsg = null;

	/** The command process builder. */
	private CommandProcessBuilder commandProcessBuilder = null;

	/** The listeners. */
	private final Set<ThreadCompleteListener> listeners
	= new CopyOnWriteArraySet<ThreadCompleteListener>();

	protected static String getEmptyJsonObj(JsonNode json, String field) {
		return MOJsonUtil.getJsonValue(json, field, null, "{}", "[]");
	}
	
	protected static String getEmptyAsDefault(JsonNode json, String field) {
		return json.has(field) ? json.get(field).asText() : "";
	}
	
	/**
	 * Instantiates a new task runnable.
	 *
	 * @param task the json task
	 * @param name the name
	 */
	public TaskRunner(TaskHandler taskHandler, Task pTask) {
		serverCommunicator = taskHandler.getServerCommunicator();
		vars = taskHandler.getRuntimeVariables();
		fileServerCommunicator = taskHandler.getFileServerCommunicator();
		
		task = pTask;
		communicationUtils = taskHandler.getCommunicationUtils();
		threadName = communicationUtils.genThreadName(pTask); 
		newTaskID = task.getUuid();
		taskUUID = task.getUuid();
		//set the retry variables
		maxTry = task.getMaxRetry();
		if (null == task.getMaxRetry() || 0 >= task.getMaxRetry())
			maxTry = 1;
		
		tryNumber = task.getRetryNumber();
		if (null == task.getRetryNumber() || 0 >= task.getRetryNumber()) 
			tryNumber = 1;
	}

	public void run() {
		logger.info("Running " + this.threadName);
		ExecutionResult runResult = null;
		String type = task.getType();
		
		if(null == task){
			runResult = new ExecutionResult(false, DIRECTFAIL, MOLogger.errorDouble(logger, "Task execution process starts with an empty task"));
		}else if (StringUtils.isEmpty(type)) {
			runResult = new ExecutionResult(false, DIRECTFAIL, MOLogger.errorDouble(logger, " Task's type is not setted, or is empty!"));
		}else if (!OSGiUtil.inited()){
			runResult = new ExecutionResult(false, DIRECTFAIL, MOLogger.errorDouble(logger, " OSGi frame work not inited!"));
		}else{
			
			//TODO 这个tryNumber控制有什么用?	目前的机制下无用,再次见到直接删除
			/*check retry before start
			 * two conditions that we can launch the task
			 * if the max retry is null, that means the settings is unlimited
			 * if the max retry is set, the execution number must be less or equal to the max retry
			 */
			MOLogger.infoDouble(logger, "Task execution process starts in " + Thread.currentThread().getName());
			this.tryNumber++;
			try {
				runResult = this.process();
			}catch(Exception e) {
				//eat the exception
				runResult = new ExecutionResult(MOLogger.errorDouble(logger, "Exception occured in task execution period! And exception class is: " + e.getClass()));
			}
		}
		
		//SGLogger.errorMessage can't use because Http bundle has been reset after the execution,and clearState has been called.
		if (null != runResult.exitCode() && 0 == runResult.exitCode()) {
			if (!runResult.bOK())
				MOLogger.errorDouble(logger, "Exit code [0], but flag is not OK.");
			else
				MOLogger.infoDouble(logger, "Exit code [TRUE] for task ["+this.newTaskID+"]");
		} else { 
			if (null != runResult.exitCode() && CHECKERROR < runResult.exitCode() && CHECKERROR + 100 > runResult.exitCode()) {
				MOLogger.errorDouble(logger, "Exit code [" + runResult.exitCode() + "] for task ["+this.newTaskID+"],and direct finish:" + directFinishRun(runResult));
			}
			//	DIRECTFAIL means should terminated in VMC,no chance to send information back to ATM.
			else if (DIRECTFAIL.equals(runResult.exitCode())) {
				MOLogger.errorDouble(logger, "directFinishRun result:" + directFinishRun(runResult));
			}
			else {
				MOLogger.errorDouble(logger, "Exit code [" + runResult.exitCode() + "] for task ["+this.newTaskID+"]");
			}
		}
		
		if (runResult.bOK()) {
			communicationUtils.doTaskUpdateToEMS(MOLogger.infoDouble(logger, runResult.msg()) + ", Task uuid:" + newTaskID.toString(), "DONE", task);
		}else {
			communicationUtils.doTaskUpdateToEMS(MOLogger.errorDouble(logger, runResult.msg()) + ", Task uuid:" + newTaskID.toString(), "ERROR", task);
		}
		setTerminated();
	}

	private String directFinishRun(ExecutionResult runResult) {
		Run[] runs = {new Run()};
		JsonNode receivedRun = task.getData();
		runs[0].setId(receivedRun.get("id").asLong());
		runs[0].setStatus("ERROR");
		runs[0].setLog(runResult.msg());
		
		return serverCommunicator.directFinishRun(runs);
	}

	private ExecutionResult doUnSupportTypeMass(String type) {
		/*the task type is not supported. we need to do the following steps.
		 * 1: set the task to error and update the log to the task
		 * 2: set the thread to be terminated
		 * 3: update worker status
		 */
		String msg = "The task's type ["+type+"] is not supported, updated to [ERROR]";
		return new ExecutionResult(MOLogger.error(logger, msg));
	}
	
	private ExecutionResult process(){
		vars.setWorkingStatus();
		String type = task.getType();
		Long timeout = task.getTimeout();
		ExecutionResult executeionResult = null;
		String msg = "Received JSON Type CMD";
		if(type.toLowerCase().startsWith("cmd")){
			msg = "Received JSON Type ["+type+"], this task will be handled as cmd type.";
			//trick
			type = "cmd";
		}else {
			msg = "Received task type:" + type.toLowerCase();
		}
		logger.info(msg);
		
		switch(type.toLowerCase()){
		case "builder":
		case "finisher":
		case "cmd":
			//TODO Keep The Old Process Way
			String command = doEscapeCharacterWork();
			processCMD(command, timeout);
			executeionResult = new ExecutionResult(true, "[NOTICE] all old types will be OK!");
			break;
	
		case "json":
			JsonNode taskData = task.getData();
			JsonNode testCase = taskData.get("testCase");
			if (null != testCase) {
				String testCaseType = testCase.get("type").asText();
				if (null != testCaseType && !testCaseType.isEmpty()) {
					switch (testCaseType.toLowerCase()) {
					case "json":
						executeionResult = processInBundles();
						break;
						//TODO 压测
					case "jmeter":
						executeionResult = pressTestInBundle();
						break;
						//TODO android测试
					case "android":
						executeionResult = androidTestInBundle();
						break;
					default:
						executeionResult = doUnSupportTypeMass(testCaseType);
						break;
					}
				}
			}
			break;
		
		default:
			executeionResult = doUnSupportTypeMass(type);
			break;
		}

		return executeionResult;
	}

	/**
	 * Do escape character work.
	 *
	 * @param taskUUID the task ID
	 * @param command the command
	 * @return the new command
	 */
	private String doEscapeCharacterWork() {
		String taskUUID = this.taskUUID.toString();
		String command = task.getData().get("command").asText();
		if(command.contains("${file.server.mounting.point}")) {
			logger.info("Found reserved valuable field  ${file.server.mounting.point} in the command will be replaced with "+vars.getFileServerMountPoint());
			logger.info("old command : "+ command);
			command = command.replace("${file.server.mounting.point}", vars.getFileServerMountPoint());
			logger.info("new command : "+ command);
		}

		if(command.contains("${taskID}")) {
			logger.info("Found reserved valuable field  ${taskID} in the command will be replaced with "+taskUUID);
			logger.info("old command : "+ command);
			command = command.replace("${taskID}", taskUUID);
			logger.info("new command : "+ command);
		}
		logger.info("final execution command is : "+ command);
		return command;
	}

	/**
	 * Gen log path.
	 *
	 * @param task the task
	 * @return the string
	 */
	private String genLogPath(ObjectNode task){
		do {
			if (JsonNodeUtils.aValidKey(task, "storages")) {
				JsonNode storage = task.get("storages");
				if (!storage.has("path")) break;

				String path = storage.get("path").asText();
				//TODO the correct key
				if (path.contains("${file.server.mounting.point}"))
					path = path.replace("${file.server.mounting.point}", vars.getFileServerMountPoint());
				return path;
			}
		}while(false);

		return vars.getFileServerMountPoint();
	}
	
	private ObjectNode getDriverInfo(Task task){
		ObjectNode taskData = (ObjectNode)task.getData();
		
		String browser = "firefox";
		String resolution = "maximize";
		ObjectNode jdbcNode = JsonNodeFactory.instance.objectNode();
		JsonNode apiNode = null;
		JsonNode zkJsonNode = null;
		
		do{
//			APP
//			Tablet
//			JDBC
//			API
//			Storage
//			WebBrowser
			
			if (taskData.has("drivers")){
				ArrayNode drivers = taskData.withArray("drivers");
				if (null != drivers && !drivers.isNull()){
					for(JsonNode d : drivers){
						MOObjectNode driver = MOObjectNode.copyNode(d);
						if (driver.has("type")){
							String type = driver.get("type").asText();
							if (type.equalsIgnoreCase("WebBrowser") || type.equalsIgnoreCase("VirtualWebBrowser")){
								if (driver.has("vendorName")){
									browser = driver.get("vendorName").asText();
								}else if (driver.has("name")){
									browser = driver.get("name").asText();
								}
								
								if (driver.has("property")){
									JsonNode properties = driver.get("property");
									if (properties.has("window.size")){
										resolution = properties.get("window.size").asText();
									}
								}
							}else if(type.equalsIgnoreCase("JDBC")){
								//TODO			
								JsonNode property = driver.get("property");
								String dburl = property.get("jdbcUrl").asText();
								jdbcNode.put("dataSourceClassName", MOJsonUtil.getDataSourceClassName(property, dburl));//property.get("dataSourceClassName").asText());
								jdbcNode.put("jdbcUrl", dburl);
								jdbcNode.put("username", property.get("username").asText());
								jdbcNode.put("password", property.get("password").asText());
								
								jdbcNode.put("id", driver.get("id").asInt());
//								jdbcNode.put("runId", driver.get("runId").asInt());
								jdbcNode.put("locatorValue", driver.get("name").asText());
								jdbcNode.put("locatorType", "Name");
								jdbcNode.put("name", driver.get("vendorName").asText());
								jdbcNode.put("type", "SQL");
							}else if (type.equalsIgnoreCase("API")) {
								apiNode = driver.get("property");
							}else if (type.equalsIgnoreCase("ZooKeeper")) {
								zkJsonNode = driver.get("property");
							}
						}
					}
				}
			}
		}while(false);
		
		ObjectNode browserNode = JsonNodeFactory.instance.objectNode();
		browserNode.put("browser", browser);
		browserNode.put("resolution", resolution);
		
		ObjectNode result = JsonNodeFactory.instance.objectNode();
		result.set("browser", browserNode);
		
		if (null != apiNode && !apiNode.isNull() && 0 != apiNode.size())
			result.set("api", apiNode);
		if (null != zkJsonNode && !zkJsonNode.isNull() && 0 != zkJsonNode.size())
			result.set("zookeeper", zkJsonNode);
		
		if (!jdbcNode.isNull() && 0 != jdbcNode.size()){
			result.set("jdbc", jdbcNode);		
		}
		
		return result;
	}
	
	/**
	 * Gen firefox path.
	 *
	 * @return the string
	 */
//	private String genFirefoxPath(){
//		//check if the 32bit firefox is installed
//		Path firefoxPath = null;
//		File x86Firefox = new File("E:/Program Files (x86)/Mozilla Firefox/firefox.exe");
//		if(x86Firefox.exists()) {
//			firefoxPath = x86Firefox.toPath();
//		}else {
//			x86Firefox = new File("D:/Program Files (x86)/Mozilla Firefox/firefox.exe");
//			if(x86Firefox.exists()) {
//				firefoxPath = x86Firefox.toPath();
//			}else {
//				x86Firefox = new File("C:/Program Files (x86)/Mozilla Firefox/firefox.exe");
//				if(x86Firefox.exists()) {
//					firefoxPath = x86Firefox.toPath();
//				}
//			}
//		}
//
//		//check if the 64bit firefox is installed
//		File x64Firefox = new File("E:/Program Files/Mozilla Firefox/firefox.exe");
//		if(x64Firefox.exists()) {
//			firefoxPath = x64Firefox.toPath();
//		}else {
//			x64Firefox = new File("D:/Program Files/Mozilla Firefox/firefox.exe");
//			if(x64Firefox.exists()) {
//				firefoxPath = x64Firefox.toPath();
//			}else {
//				x64Firefox = new File("C:/Program Files/Mozilla Firefox/firefox.exe");
//				if(x64Firefox.exists()) {
//					firefoxPath = x64Firefox.toPath();
//				}
//			}
//		}
//
//		return null == firefoxPath ? "" : firefoxPath.toString();
//	}
//	
//	private String genChromePath(){
//		//check if the 32bit firefox is installed
//		Path chromePath = null;
//		File x86Chrome = new File("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
//		if(x86Chrome.exists()) {
//			chromePath = x86Chrome.toPath();
//		}
//
//		//check if the 64bit firefox is installed
//		File x64Chrome = new File("C:/Program Files/Google/Chrome/Application/chrome.exe");
//		if(x64Chrome.exists()) {
//			chromePath = x64Chrome.toPath();
//		}
//
//		return null == chromePath ? "" : chromePath.toString();
//	}
//	
//	private String getIEPath(){
//		//check if the (32bit IE of 32bit system / 64bit IE of 64bit system) is installed
//		Path iePath = null;
//		File exactlyIE = new File("C:/Program Files/Internet Explorer/iexplore.exe");
//		if(exactlyIE.exists()) {
//			iePath = exactlyIE.toPath();
//		}else{
//			//check if the 32bit IE is installed in 64bit system if the prevsius one is not installed.
//			File x64IE = new File("C:/Program Files (x86)/Internet Explorer/iexplore.exe");
//			if(x64IE.exists()) {
//				iePath = x64IE.toPath();
//			}
//		}
//
//		return null == iePath ? "" : iePath.toString();
//	}
	
	private ExecutionResult pressTestInBundle() {
		String msgFromProcessExecution = null;
		ExecutionResult executionResult = new ExecutionResult("has not execute task");
		try{
			//data为空
			if (NullNode.getInstance() == task.getData()){
				return new ExecutionResult("[ERROR] The task's data is null");
			}
			ObjectNode taskDataContent = getTaskData(task);
			String runType = task.getData().get("type").asText();	//PROCUDTION or DEV
			long runId = task.getData().get("id").asLong();

			//确保json中的基本字段有效
			//TODO 通过json schema校验
			executionResult = checkRequiredKeysExisting(taskDataContent);
			if (!executionResult.bOK()) return executionResult;

			executionResult = logPathOK(genLogPath(taskDataContent));
			if (!executionResult.bOK()) return executionResult;

			//TODO
			//组配数据为web driver接受的格式
			ObjectNode bundleTaskParams = rebuildWebDriverBundleData(task, taskDataContent, taskUUID, runType, runId);
			String commandToBundle = bundleTaskParams.toString();
			logger.info("the data to web driver bundle:" + commandToBundle);
			TestCaseExecutor executor = new JmeterTestCaseExecutor(this, "jmeter", task.getData(), task.getUuid(), bundleTaskParams);
			//写到本地备份
			
			writeOldTaskAtLocal(commandToBundle);
			executionResult = executor.executePressTest();
		}
		catch (Exception e) {
			msgFromProcessExecution = "[Task Execution Exception] with " + e.getClass().toString();
			executionResult = new ExecutionResult(false, 1, msgFromProcessExecution);
			e.printStackTrace();
		}
		finally{
			setTerminated();
		}
		return executionResult;
	}
	
	private ExecutionResult androidTestInBundle() {
		String msgFromProcessExecution = null;
		ExecutionResult executionResult = new ExecutionResult("has not execute task");
		try{
			//data为空
			if (NullNode.getInstance() == task.getData()){
				return new ExecutionResult("[ERROR] The task's data is null");
			}
			ObjectNode taskDataContent = getTaskData(task);
			String runType = task.getData().get("type").asText();	//PROCUDTION or DEV
			long runId = task.getData().get("id").asLong();

			//确保json中的基本字段有效
			//TODO 通过json schema校验
			executionResult = checkRequiredKeysExisting(taskDataContent);
			if (!executionResult.bOK()) return executionResult;

			executionResult = logPathOK(genLogPath(taskDataContent));
			if (!executionResult.bOK()) return executionResult;

			//TODO
			//组配数据为web driver接受的格式
			ObjectNode bundleTaskParams = rebuildWebDriverBundleData(task, taskDataContent, taskUUID, runType, runId);
			String commandToBundle = bundleTaskParams.toString();
			logger.info("the data to web driver bundle:" + commandToBundle);
			TestCaseExecutor executor = new TestCaseExecutor(this, task.getType(), task.getData(), task.getUuid(), bundleTaskParams);
			//写到本地备份
			if (null != executor.instructionList) {
				writeNewTaskAtLocal(executor.instructionList.toString());
			}
			writeOldTaskAtLocal(commandToBundle);
			executionResult = executor.execute();
		}
		catch (Exception e) {
			msgFromProcessExecution = "[Task Execution Exception] with " + e.getClass().toString();
			executionResult = new ExecutionResult(false, 1, msgFromProcessExecution);
			e.printStackTrace();
		}
		finally{
			setTerminated();
		}
		return executionResult;
	}
	
	private ExecutionResult processInBundles() {
		String msgFromProcessExecution = null;
		ExecutionResult executionResult = new ExecutionResult("has not executed task");
		do {
			try {
				// data为空
				if (NullNode.getInstance() == task.getData()) {
					return new ExecutionResult(MOLogger.error(logger, "[ERROR] The task's data is null"));
				}

				ObjectNode taskDataContent = getTaskData(task);
				String runType = task.getData().get("type").asText(); // PROCUDTION or DEV
				long runId = task.getData().get("id").asLong();

				// 确保json中的基本字段有效
				// TODO 通过json schema校验
				executionResult = checkRequiredKeysExisting(taskDataContent);
				if (!executionResult.bOK()) break;
				executionResult = logPathOK(genLogPath(taskDataContent));
				if (!executionResult.bOK())break;

				// TODO
				// 组配数据为web driver接受的格式
				ObjectNode bundleTaskParams = rebuildWebDriverBundleDataNew(task, taskDataContent, taskUUID, runType, runId);
				String commandToBundle = bundleTaskParams.toString();
				logger.info("The data to web driver bundle:" + commandToBundle);
				TestCaseExecutor executor = new TestCaseExecutor(this, task.getType(), task.getData(), task.getUuid(), bundleTaskParams);
				
				// 写到本地备份
				writeNewTaskAtLocal(executor.instructionList.toString());
				writeOldTaskAtLocal(commandToBundle);

				executionResult = executor.execute();
			} catch (VMCJsonFieldNotExistException e) {
				msgFromProcessExecution = "[Task Execution JSON Field Exception] with " + e.getMessage();
				executionResult = new ExecutionResult(false, DIRECTFAIL, msgFromProcessExecution);
				e.printStackTrace();
			} catch (VMCJsonSchemaException e) {
				msgFromProcessExecution = "[Task Execution JSON Exception] with " + e.getMessage();
				executionResult = new ExecutionResult(false, DIRECTFAIL, msgFromProcessExecution);
				e.printStackTrace();
			} catch (VMCLogicalException e) {
				msgFromProcessExecution = "[Task Execution Logical Exception] with " + e.getMessage();
				executionResult = new ExecutionResult(false, DIRECTFAIL, msgFromProcessExecution);
				e.printStackTrace();
			} catch (NullPointerException e) {
				StackTraceElement[] stes = e.getStackTrace();
				if (null != stes && 0 < stes.length) {
					StackTraceElement ste = stes[stes.length - 1];
					if (StringUtils.isEmpty(e.getMessage()))
						msgFromProcessExecution = "[Task Execution Null Point Exception] with " + ste.toString();
					else
						msgFromProcessExecution = "[Task Execution Null Point Exception] with " + e.getMessage();
				} else {
					msgFromProcessExecution = "[Task Execution Null Point Exception] with " + e.getMessage();
				}
				executionResult = new ExecutionResult(false, DIRECTFAIL, msgFromProcessExecution);
				e.printStackTrace();
			} catch (Exception e) {
				msgFromProcessExecution = "[Task Execution Exception] with " + e.getClass().toString();
				executionResult = new ExecutionResult(false, 1, msgFromProcessExecution);
				e.printStackTrace();
			} finally {
				setTerminated();
			}
		} while (false);
		return executionResult;
	}

	private ExecutionResult logPathOK(String path) {
		if (!localAccessable(path)) {
			return new ExecutionResult(false, MOLogger.error(logger, String.format("[Local Log Folder] [%s] is not valid.", path)));
		}
		return new ExecutionResult();
	}

	private void writeOldTaskAtLocal(String commandToBundle) {
		String firstFile = "C:\\vmctask\\commandToBundle.txt";
		String secondFile = "commandToBundle.txt";
		if (PlatformUtil.linux())
			firstFile = secondFile;
		
		if (vars.writeTask()){
			try{
				File outFile = new File(firstFile);
				OutputStream os = new FileOutputStream(outFile);
				os.write(commandToBundle.getBytes());
				os.close();
			}catch (Exception e) {
				logger.error("[First Try] Write Task Data in Json format fail.");
				try{
					File outFile = new File(secondFile);
					OutputStream os = new FileOutputStream(outFile);
					os.write(commandToBundle.getBytes());
					os.close();
				}catch(Exception e1){
					logger.error("[Second Try] Write Task Data in Json format fail.");
				}
			}
		}
	}
	
	private void writeNewTaskAtLocal(String commandToBundle) {
		String firstFile = "C:\\vmctask\\commandToBundleNew.txt";
		String secondFile = "commandToBundleNew.txt";
		
		if (PlatformUtil.linux())
			firstFile = secondFile;
		
		if (vars.writeTask()){
			try{
				File outFile = new File(firstFile);
				OutputStream os = new FileOutputStream(outFile);
				os.write(commandToBundle.getBytes());
				os.close();
			}catch (Exception e) {
				logger.error("[First Try] Write Task Data in Json format fail.");
				try{
					File outFile = new File(secondFile);
					OutputStream os = new FileOutputStream(outFile);
					os.write(commandToBundle.getBytes());
					os.close();
				}catch(Exception e1){
					logger.error("[Second Try] Write Task Data in Json format fail.");
				}
			}
		}
	}

	private ObjectNode getTaskData(Task task) {
		ObjectNode taskDataContent = (ObjectNode) task.getData();//TODO ["data"]["data"]??
		if (null != taskDataContent && taskDataContent.has("testCaseId") && taskDataContent.has("testCase")){
			taskDataContent = (ObjectNode)taskDataContent.get("testCase");
		}
		return taskDataContent;
	}
	
	private boolean localAccessable(String logFolder){
		File tmp = new File(logFolder);
		if (!tmp.exists() || !tmp.canWrite() || tmp.isFile())
			return false;
		return true;
	}

	/**
	 * assemble the data format ,for the web driver bundle `s need.
	 * @param task
	 * @param taskDataContent
	 * @return
	 */
	private ObjectNode rebuildWebDriverBundleDataNew(Task task, ObjectNode taskDataContent, UUID taskUUID, String runType, long runId) {
		ObjectNode bundleTaskParams = JsonNodeFactory.instance.objectNode();
		
		bundleTaskParams.put("logFileToServer", vars.logFileToServer());
		bundleTaskParams.put("logFolder", genLogPath(taskDataContent));
		bundleTaskParams.put("name", task.getName());
		
		//video
		addDataSourceIfNeed(bundleTaskParams, getDriverInfo(task));
		bundleTaskParams.set("parameters", vars.genReportBackParamsters(taskDataContent, runType, runId));
		bundleTaskParams.put("recordVideo", taskDataContent.has("is_recorded") ? taskDataContent.get("is_recorded").asBoolean() : false);
		bundleTaskParams.put("runId", runId);
		bundleTaskParams.put("standSingleton", vars.standSingleton());
		bundleTaskParams.set("taskData", taskDataContent);
		bundleTaskParams.put("uuid", taskUUID.toString());

		return bundleTaskParams;
	}
	
	/**
	 * assemble the data format ,for the web driver bundle `s need.
	 * @param task
	 * @param taskDataContent
	 * @return
	 */
	private ObjectNode rebuildWebDriverBundleData(Task task, ObjectNode taskDataContent, UUID taskUUID, String runType, long runId) {
		ObjectNode bundleTaskParams = JsonNodeFactory.instance.objectNode();
		
		bundleTaskParams.put("name", task.getName());
		bundleTaskParams.put("uuid", taskUUID.toString());
		bundleTaskParams.put("standSingleton", vars.standSingleton());
		bundleTaskParams.put("logFileToServer", vars.logFileToServer());
		
		//video
		bundleTaskParams.put("recordVideo", taskDataContent.has("is_recorded") ? taskDataContent.get("is_recorded").asBoolean() : false);
		
		//分辨率
		//TODO
//		JsonNode driver = getDriverInfo(task);
//		JsonNode drivers = null;
		
		//TODO ZooKeeper
//		if (task.getData().has("drivers")){
//			drivers = task.getData().get("drivers");
//			bundleTaskParams.set("driver", driver);
//		}
//		JsonNode browser = driver.get("browser");
//		bundleTaskParams.put("resolution", browser.get("resolution").asText());
//		bundleTaskParams.put("browser", browser.get("browser").asText());
		addDataSourceIfNeed(bundleTaskParams, getDriverInfo(task));
		
//		bundleTaskParams.put("firefoxPath", genFirefoxPath());
//		bundleTaskParams.put("chromePath", genChromePath());
//		bundleTaskParams.put("iePath", getIEPath());
		
//		bundleTaskParams.put("geckodriverPath", genGeckodriverPath());
//		bundleTaskParams.put("chromedriverPath", genChromedriverPath());
//		bundleTaskParams.put("edgedriverPath", genEdgedriverPath());
//		bundleTaskParams.put("iedriverPath", genIEdriverPath());
		
		bundleTaskParams.put("logFolder", genLogPath(taskDataContent));//file_server_mounting_point
		bundleTaskParams.set("taskData", taskDataContent);
		bundleTaskParams.set("parameters", vars.genReportBackParamsters(taskDataContent, runType, runId));
		bundleTaskParams.put("runId", runId);

		return bundleTaskParams;
	}

	private JsonNode addDataSourceIfNeed(ObjectNode bundleTaskParams, JsonNode driver) {
		JsonNode jdbc = null;
		if (driver.has("jdbc")){
			jdbc = driver.get("jdbc");
			String url = jdbc.get("jdbcUrl").asText();
			String className = MOJsonUtil.getDataSourceClassName(jdbc, url);
			if (className.isEmpty()){
				
			}else{
				String user = jdbc.get("username").asText();
				String pwd = jdbc.get("password").asText();
				String dsName = jdbc.get("locatorValue").asText();
				
				bundleTaskParams.put("dataSourceClassName", className);
				bundleTaskParams.put("jdbcUrl", url);
				bundleTaskParams.put("username", user);
				bundleTaskParams.put("password", pwd);
				bundleTaskParams.put("dsName", dsName);
				
				vars.dbUrl = url;
				vars.dbUser = user;
				vars.dbPwd = pwd;
				vars.dbClassName = className;
				vars.dbName = dsName;
				vars.bNewDataBase = true;
			}
		}
		return jdbc;
	}

//	private ObjectNode instrucitonArrayInJsonObj(ObjectNode taskDataContent, JsonNode jdbc) {
//		ArrayNode instructions = taskDataContent.withArray("instructions");
//		ArrayNode results = JsonNodeFactory.instance.arrayNode();
//
//		String[] headerFuncitons = {"Type", "Comment", "Object", "Action", "Input", "Options", "Target", "InstructionId", "LogicalOrderIndex"};
//		//for 接口测试,统一进webdriver的测试循环
////		String[] headerAPIs = {"Type", "Action", "Input", "Protocol", "Host", "Port", "BaseUrl", 
////				"InstructionId", "RequestHeaders", "RequestBody", "QueryParameters", "HttpResponseCode", 
////				"JsonSchema", "LogicalOrderIndex"};
//		String[] headerAPIs = {"logicalOrderIndex", "input", "data", "type"};//, "elementAction""action", 
//		String[] headerAPIDatas = {"method", "protocol", "responseCode", "url", "queryParameters"};	//"requestHeaders", 
//
//		boolean sqlTypeExist = false;
//		for (JsonNode instruction : instructions) {
//			if (instruction.get("type").asText().equalsIgnoreCase("Manual")) continue;
//			
//			String instructionType = instruction.get("type").asText();
//
//			ObjectNode newInstruction = JsonNodeFactory.instance.objectNode();
//			newInstruction.put("Type", instructionType);
//			newInstruction.put("Action", MOJsonUtil.getJsonValue(instruction, "action", null, "Execute", ""));
//			newInstruction.put("Input", instruction.get("input").asText());
//			newInstruction.put("InstructionId", instruction.get("id").asText());
//			//加入instruction顺序,用以回传。考虑循环执行
//			newInstruction.put("LogicalOrderIndex", instruction.get("logicalOrderIndex").asText());
//			newInstruction.put("Options", JsonNodeUtils.genOptionsStr(instruction));
//	
//			instructionType = instructionType.toLowerCase();
//			if (InstructionUtils.webType(instructionType)){
//				if (!sqlTypeExist && instructionType.equals("sql")) sqlTypeExist = true;
//				if (!instruction.has("element")) continue;
//				//TODO SQL类型语句,使用Driver作为Element信息
//				if (null != jdbc && !jdbc.isNull() && instruction.get("element").get("type").asText().equalsIgnoreCase("SQL") ){
//					newInstruction.put("Comment", instruction.get("comment").asText());
//					newInstruction.put("Object", jdbc.get("id").asInt());		//object like a.b.c.d
//					newInstruction.put("Target", instruction.get("target").asText());
//				}else{
//					newInstruction.put("Comment", instruction.get("comment").asText());
//					newInstruction.put("Object", String.valueOf(instruction.get("elementId").asInt()));		//object like a.b.c.d
//					newInstruction.put("Target", instruction.get("target").asText());
//				}
//			} else if (InstructionUtils.apiType(instructionType)) {
//				
//				JsonNode data = instruction.get("data");
//				newInstruction.put("RequestHeaders", getEmptyJsonObj(data, "requestHeaders"));
//				newInstruction.put("RequestBody", getEmptyJsonObj(data, "body"));
//				newInstruction.put("QueryParameters", getEmptyJsonObj(data, "queryParameters"));
//				if (data.get("responseCode").isNull())
//					newInstruction.put("HttpResponseCode", "[200,300)");
//				else
//					newInstruction.put("HttpResponseCode", data.get("responseCode").asText());
//				//json schema
//				JsonNode jsonNode = data.get("jsonSchema");
//				if (null == jsonNode || jsonNode.isNull()){
//					newInstruction.put("JsonSchema", JsonNodeFactory.instance.nullNode().toString());
//				}else{
//					newInstruction.put("JsonSchema", jsonNode.toString());
//				}
//				//json node
//				JsonNode jsonpathNode = data.get("jsonPathPackage");
//				if (null == jsonpathNode || jsonpathNode.isNull()){
//					newInstruction.put("JsonPathPackage", JsonNodeFactory.instance.nullNode().toString());
//				}else{
//					newInstruction.put("JsonPathPackage", jsonpathNode.toString());
//				}
//				
//				newInstruction.put("Protocol", data.get("protocol").asText());
//				newInstruction.put("Host", getEmptyAsDefault(data, "host"));
//				newInstruction.put("Port", getEmptyAsDefault(data, "port"));
//				newInstruction.put("BaseUrl", getEmptyAsDefault(data, "baseUrl"));
//				//转换
//				String input = instruction.get("input").asText();
//				if (!input.contains(data.get("protocol").asText())){
//					input = data.get("protocol").asText() + input;
//				}
//				newInstruction.put("Url", input);
//			} else if (InstructionUtils.expressionType(instructionType)) {
//				// do nothing extra for expression
//			}
//			results.add(newInstruction);
//		}
//		
//		//TODO 只有含有sql类型的语句时,才需要在VMC中初始化ds
//		if (sqlTypeExist){
//			if (vars.bNewDataBase){
//				//保证足够Lazy，只能在webdriver中实际使用时再add，否则冗余下发的jdbc driver将不会走释放逻辑
//				//vars.publisher.publishDataSource("go");
//				OSGiUtil.innerAddDataSource(vars.dbUrl, vars.dbUser, vars.dbPwd, vars.dbClassName, vars.dbName);
//			}
//		}
//		
//		ObjectNode instructionsContainer = JsonNodeFactory.instance.objectNode();
//		instructionsContainer.set("Instructions", results);
//		return instructionsContainer;
//	}

	//TODO
	private String genGeckodriverPath() {
		if (PlatformUtil.linux())
			return System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "VMCDrivers" + File.separator + "geckodriver";
		if (PlatformUtil.windows())
			return System.getProperty("user.home") + "\\Desktop\\VMCDrivers\\geckodriver-windows-64.exe";
		return "";
	}
	
	private String genChromedriverPath() {
		if (PlatformUtil.linux())
			return System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "VMCDrivers" + File.separator + "chromedriver";
		if (PlatformUtil.windows())
			return System.getProperty("user.home") + "\\Desktop\\VMCDrivers\\chromedriver-windows-32.exe";
		return "";
	}
	
	private String genEdgedriverPath() {
		return System.getProperty("user.home") + "\\Desktop\\VMCDrivers\\MicrosoftWebDriver.exe";
	}
	
	private String genIEdriverPath() {
		return System.getProperty("user.home") + "\\Desktop\\VMCDrivers\\iedriver-windows-64.exe";
	}

	private InstructionTypeCnf getInstructionTypeCnf(JsonNode instruction) {

		int instructionType = 0;//1 function(sql,javascript); 2 rest_api; 3 MathExpressionProcessor/StringDataProcessor; 6 StringUtil
		String iType = instruction.get("type").asText().toLowerCase();
		if (apiTypes.contains(iType))
			instructionType = 2;
		else if (functionTypes.contains(iType))
			instructionType = 1;
		else if (expressionTypes.contains(iType))
			instructionType = 3;
		else if (redisTypes.contains(iType))
			instructionType = 4;
		else if (rpcDubboTypes.contains(iType))
			instructionType = 5;
	    else if (utilTypes.contains(iType))
	        instructionType = 6;
	    else if (emailTypes.contains(iType))
	    	instructionType = 7;

//		String[] instructionKeys = bFunction ? functionInstructionKeys : apiInstructionKeys;
		String[] instructionKeys = functionInstructionKeys;
		if (2 == instructionType) instructionKeys = apiInstructionKeys;
		else if (3 == instructionType) instructionKeys = expressionInstructionKeys;
		else if (4 == instructionType) instructionKeys = redisIntructionKeys;
		else if (5 == instructionType) instructionKeys = rpcDubboInstructionKeys;
		else if (6 == instructionType) instructionKeys = utilInstructionKeys;
		else if (7 == instructionType) instructionKeys = emailInstructionKeys;
		
		return new InstructionTypeCnf(instructionType, instructionKeys);
	}
	
	// TODO 使用jsonschema替代这种校验(需要task json字段稳定)
	private ExecutionResult checkRequiredKeysExisting(ObjectNode taskDataContent) {
		//TODO use json schema here , see file2JSON JsonValidationExample
		String[] commandKeys = {"name", "instructions", "group"};//, "systemRequirements"};//"engines", "environments"};//"storages",
		String[] functionInstructionElementKeys = {"id", "name", "locatorValue", "type", "locatorType"};
		String[] apiInstructionDataKeys = {"method", "protocol", "responseCode", "url"};//, "requestHeaders", "queryParameters"
		String[] rpcDubboInstructionDataKeys = {"dubboServiceInterfaceName", "dubboServiceInterfaceMethod", "dubboServiceInterfaceParamters"};
		String[] checkEmailInstructionDataKeys = {"sender", "address", "content", "subject", "timeSpan", "contentCheckType", "subjectCheckType"};
		
		boolean pass = true;
		String errBackToEMS = "";
		for(String key : commandKeys)
			if (!taskDataContent.has(key)) {
				this.executionExitCode = CHECKERROR + 1;
				pass = false;
				errBackToEMS = String.format("Task data parameters not OK,task UUID:%s, required fields:[%s], but field:[%s] lost !!!", taskUUID.toString(), Arrays.toString(commandKeys), key);
				break;
			}else if(key.equalsIgnoreCase("instructions")){
				JsonNode instructions = taskDataContent.get("instructions");
				if (null == instructions || NullNode.instance == instructions || !instructions.isArray()){
					this.executionExitCode = CHECKERROR + 2;
					pass = false;
					errBackToEMS = "instruction is null or not a array";
					break;
				}else{
					ArrayNode instructionArr = (ArrayNode)instructions;
					for(JsonNode ins : instructionArr){
						MOObjectNode instruction = MOObjectNode.copyNode(ins);
						if (!instruction.has("type")) {
							this.executionExitCode = CHECKERROR + 3;
							pass = false;
							errBackToEMS = "instruction need field [type]";
							break;
						}
						
						InstructionTypeCnf insCnf = getInstructionTypeCnf(instruction);
						
						for (String instructionKey : insCnf.keys) {
							if (!instruction.has(instructionKey)) {
								this.executionExitCode = CHECKERROR + 4;
								pass = false;
								errBackToEMS = "instruction need field [" + instructionKey + "]";
								break;
							} else if (1 == insCnf.iType && instructionKey.equalsIgnoreCase("element")) {
								JsonNode element = instruction.get("element");

								for (String elementKey : functionInstructionElementKeys) {
									// TODO 跳过Driver型element
									if (element.get("isDriver").asBoolean() && element.get("id").asInt() < 1000) {
										continue;
									}
									if (!element.has(elementKey)) {
										this.executionExitCode = CHECKERROR + 5;
										pass = false;
										errBackToEMS = "element in instruction need field [" + elementKey + "]";
										break;
									}
								}
								if (false == pass)
									break;
							}
							//API
							else if (2 == insCnf.iType && instructionKey.equalsIgnoreCase("data")) {
								JsonNode data = instruction.get("data");
								for (String dataKey : apiInstructionDataKeys) {
									if (!data.has(dataKey)) {
										// TODO
										if (dataKey.equalsIgnoreCase("protocol")) {
											if (data.has("url")) {
												if (data.get("url").asText().toLowerCase().startsWith("https")) {
													((ObjectNode) data).put("protocol", "https");
													continue;
												} else if (data.get("url").asText().toLowerCase().startsWith("http")) {
													((ObjectNode) data).put("protocol", "http");
													continue;
												}
											}
										}
										this.executionExitCode = CHECKERROR + 6;
										pass = false;
										errBackToEMS = "data in api instruction need field [" + dataKey + "]";
										break;
									}
								}
								if (false == pass)
									break;
							}
							//RPC dubbo
							else if (5 == insCnf.iType && instructionKey.equalsIgnoreCase("data")) {
								JsonNode data = instruction.get("data");
								for (String dataKey : rpcDubboInstructionDataKeys) {
									if (!data.has(dataKey)) {
										this.executionExitCode = CHECKERROR + 7;
										pass = false;
										errBackToEMS = "data in rpc-dubbo instruction need field [" + dataKey + "]";
										break;
									}
								}
								if (false == pass)
									break;
							}
							//Check Email
							else if(7 == insCnf.iType && instructionKey.equalsIgnoreCase("data")) {
								JsonNode data = instruction.get("data");
								for (String dataKey : checkEmailInstructionDataKeys) {
									if (!data.has(dataKey)) {
										this.executionExitCode = CHECKERROR + 9;
										pass = false;
										errBackToEMS = "data in check-email instruction need field [" + dataKey + "]";
										break;
									}
								}
								if (false == pass)
									break;
							}
						}
						if (false == pass) break;
					}
					if (false == pass) break;
				}
			}
		
		if (false == pass){
			errBackToEMS = "parameters not OK! " + errBackToEMS;
			return new ExecutionResult(false, (null != this.executionExitCode ? this.executionExitCode : -1), MOLogger.error(logger, errBackToEMS));
		}
		return new ExecutionResult(pass, errBackToEMS);
	}

	/**
	 * Process CMD.
	 *
	 * @param taskID the task ID
	 * @param taskUUID the task UUID
	 * @param command the command
	 * @param timeout the timeout
	 * @param unit the unit
	 */
	/*
	 * execute the task and return the result.
	 * 
	 */
	private void processCMD(String command, Long timeout){
		String msgFromProcessExecution = null; 
		try{
			//create a processbuiler to execute the command.
			List<String> commandTokens = new ArrayList<String>();
			Matcher m = Pattern.compile("([^\\\"]\\S*|\\\"\\\"|\\\".*?[^\\\\]\\\")\\s*").matcher(command);
			while (m.find())
				commandTokens.add(m.group(1));
			logger.info("Executing command ["+command+"]");
			logger.debug("Tokenlized command : ["+commandTokens+"]");
			commandProcessBuilder = new CommandProcessBuilder(null, commandTokens, timeout);
//			updateTaskToManager(taskUUID);
			int executionStatusResult = commandProcessBuilder.execute();
			logger.info("Task ["+newTaskID+"] execution status result is "+executionStatusResult);
			//the execution is done
			/*return a status code
			 * 0 : normal terminated
			 * 1 : execution timeout
			 * 2 : Error
			 * */
			if(executionStatusResult == 0){
				/*this can have two different situations.
				 * 1 : zero exitcode
				 * 2 : non zero exitcode
				 */
				//get the exit code first
				Integer exitCode = commandProcessBuilder.getExitCode();
				this.executionExitCode = exitCode;
				msgFromProcessExecution = commandProcessBuilder.getMessage();
				this.executionMsg = msgFromProcessExecution;
				logger.info("Successfully executed the command ["+command+"]");
				logger.info("Exit code for task ["+this.taskID+"] is "+exitCode);
				//save the execution log
				ObjectNode jsonExecutionLogUpload = JsonNodeFactory.instance.objectNode();
				jsonExecutionLogUpload.put("inputStream", commandProcessBuilder.getInfos());
				jsonExecutionLogUpload.put("errorStream", commandProcessBuilder.getErrors()+"\n"+msgFromProcessExecution);
				fileServerCommunicator.saveExecutionLog(taskID, jsonExecutionLogUpload);
				
			}else if(executionStatusResult == 1){
				//get the message from the executor
				Integer exitCode = commandProcessBuilder.getExitCode();
				msgFromProcessExecution = commandProcessBuilder.getMessage();
				this.executionMsg = msgFromProcessExecution;
				logger.error("Timedout executing the command ["+command+"] with message ["+msgFromProcessExecution+"]");
				logger.error("Exit code for task ["+this.taskID+"] is "+exitCode);							
				//save the execution log
				ObjectNode jsonExecutionLogUpload = JsonNodeFactory.instance.objectNode();
				jsonExecutionLogUpload.put("inputStream", commandProcessBuilder.getInfos());
				jsonExecutionLogUpload.put("errorStream", commandProcessBuilder.getErrors()+"\n"+msgFromProcessExecution);
				fileServerCommunicator.saveExecutionLog(taskID, jsonExecutionLogUpload);
				//set the internal timeout flag to true
				this.processTimeout = true;
				
			}else if(executionStatusResult == 2 ){
				//get the message from the executor
				Integer exitCode = commandProcessBuilder.getExitCode();
				msgFromProcessExecution =  commandProcessBuilder.getMessage();
				this.executionMsg = msgFromProcessExecution;
				logger.error("Error executing the command ["+command+"] with message ["+msgFromProcessExecution+"]");
				logger.error("Exit code for task ["+this.taskID+"] is "+exitCode);		

				//save the execution log
				ObjectNode jsonExecutionLogUpload = JsonNodeFactory.instance.objectNode();
				jsonExecutionLogUpload.put("inputStream", commandProcessBuilder.getInfos());
				jsonExecutionLogUpload.put("errorStream", commandProcessBuilder.getErrors()+"\n"+msgFromProcessExecution);
				fileServerCommunicator.saveExecutionLog(taskID, jsonExecutionLogUpload);
				vars.updateWorkerStatusSpringBootWay();
			}else{
				Integer exitCode = commandProcessBuilder.getExitCode();
				msgFromProcessExecution =  commandProcessBuilder.getMessage();
				this.executionMsg = msgFromProcessExecution;
				ObjectNode jsonExecutionLogUpload = JsonNodeFactory.instance.objectNode();
				jsonExecutionLogUpload.put("inputStream", commandProcessBuilder.getInfos());
				jsonExecutionLogUpload.put("errorStream", commandProcessBuilder.getErrors()+"\n"+msgFromProcessExecution);
				fileServerCommunicator.saveExecutionLog(taskID, jsonExecutionLogUpload);
				logger.error("Unknown excutionStatusResult, Error executing the command ["+command+"] with message ["+msgFromProcessExecution+"]");
				logger.error("Exit code for task ["+this.taskID+"] is "+exitCode);					
				vars.updateWorkerStatusSpringBootWay();
			}
		}catch(Exception e) {
			
		}finally{
			if(commandProcessBuilder != null){
				logger.info("TaskRunnable : kill the task ["+taskID+"]");
				commandProcessBuilder.kill();
				commandProcessBuilder.setSeError(null);
				commandProcessBuilder.setSeInfo(null);
				commandProcessBuilder = null;
			}
			//update the listeners to terminated.
			notifyListeners();
			setTerminated();
		}	
	}

	/**
	 * Adds the listener.
	 *
	 * @param listener the listener
	 */
	public final void addListener(final ThreadCompleteListener listener) {
		listeners.add(listener);
	}


	/**
	 * Removes the listener.
	 *
	 * @param listener the listener
	 */
	public final void removeListener(final ThreadCompleteListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify listeners.
	 */
	private final void notifyListeners() {
		for (ThreadCompleteListener listener : listeners) {
			listener.setTerminated(true);;
		}
	}

	/**
	 * Checks if is triggered.
	 *
	 * @return true, if is triggered
	 */
	public boolean isTriggered() {
		return triggered;
	}

	/**
	 * Stop.
	 */
	public void stop(){
		Thread killSignal = this.taskThread;
		this.taskThread = null;
		killSignal.interrupt();			
	}

	/**
	 * Checks if is timeouted.
	 *
	 * @return true, if is timeouted
	 */
	public boolean isTimeouted() {

		if(this.processTimeout){
			return true;
		}
		Long timeout = null;
		if(this.task.getTimeout() != null){
			timeout = this.task.getTimeout();
		}
		if(timeout == null ){
			return false;
		}

		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime timeoutCal = this.launchedTime.plus(timeout, ChronoUnit.SECONDS);

		//add 5 minutes to the timeout to give process a chance to timeout naturally.
		//timeoutCal = timeoutCal.minusSeconds(20);                                      
		timeoutCal = timeoutCal.plusMinutes(5);

		if(currentTime.isAfter(timeoutCal)){
			logger.info("Task [" + newTaskID + "] Timedout by vm-manager.");
			//save the execution log
			String msgFromProcessExecution =  commandProcessBuilder.getMessage();				
			msgFromProcessExecution += "Process destroyed by vm-manager due to timeout "+ timeout.toString() +" "+ TimeUnit.SECONDS.toString();
			this.executionMsg = msgFromProcessExecution;
			ObjectNode jsonExecutionLogUpload = JsonNodeFactory.instance.objectNode();
			jsonExecutionLogUpload.put("inputStream", commandProcessBuilder.getInfos());
			jsonExecutionLogUpload.put("errorStream", commandProcessBuilder.getErrors()+"\n"+msgFromProcessExecution);
			fileServerCommunicator.saveExecutionLog(taskID, jsonExecutionLogUpload);
			//kill the tpb
			if(commandProcessBuilder != null && commandProcessBuilder.isAlive()){
				commandProcessBuilder.kill();
				commandProcessBuilder.setSeError(null);
				commandProcessBuilder.setSeInfo(null);
				commandProcessBuilder = null;
			}
			this.terminated = true;
			this.processTimeout = true;
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Gets the thread name.
	 *
	 * @return the thread name
	 */
	public String getThreadName() {
		return this.threadName;
	}

	/**
	 * Checks if is terminated.
	 *
	 * @return true, if is terminated
	 */
	public boolean isTerminated() {
		return this.terminated;
	}

	/**
	 * Gets the task ID.
	 *
	 * @return the task ID
	 */
	public long getTaskID(){
		return this.taskID;
	}

	/**
	 * Gets the task.
	 *
	 * @return the task
	 */
	public Task getTask(){
		return this.task;
	}

	/**
	 * Reset thread status.
	 */
	public void resetThreadStatus(){
		this.processTimeout = false;
		this.terminated = false;
	}

	/**
	 * Gets the tries.
	 *
	 * @return the tries
	 */
	public int getTries() {
		return tryNumber;
	}

	/**
	 * Gets the max try.
	 *
	 * @return the max try
	 */
	public Integer getMaxTry() {
		return maxTry;
	}

	/**
	 * Gets the execution exit code.
	 *
	 * @return the execution exit code
	 */
	public Integer getExecutionExitCode() {
		return executionExitCode;
	}

	/**
	 * Gets the execution status code.
	 *
	 * @return the execution status code
	 */
	public int getExecutionStatusCode() {
		return executionStatusCode;
	}

	/**
	 * Gets the task UUID.
	 *
	 * @return the task UUID
	 */
	public UUID getTaskUUID() {
		return taskUUID;
	}

	/**
	 * Gets the execution msg.
	 *
	 * @return the execution msg
	 */
//	public String getExecutionMsg() {
//		return executionMsg;
//	}

	/**
	 * Sets the execution msg.
	 *
	 * @param executionMsg the new execution msg
	 */
//	public void setExecutionMsg(String executionMsg) {
//		this.executionMsg = executionMsg;
//	}

	/**
	 * Sets the terminated.
	 *
	 * @param terminated the new terminated
	 */
	public synchronized void setTerminated() {
		this.task.setExecutionEndAt(new Date());
		this.terminated = true;
	}
}
