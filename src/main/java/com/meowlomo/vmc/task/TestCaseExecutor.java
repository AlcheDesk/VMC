package com.meowlomo.vmc.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.exception.VMCJsonSchemaException;
import com.meowlomo.vmc.exception.VMCLogicalException;
import com.meowlomo.vmc.osgi.OSGiUtil;
import com.meowlomo.vmc.util.MOCommonUtil;
import com.meowlomo.vmc.util.MOObjectNode;
import com.sun.jna.platform.win32.COM.util.ObjectFactory;

public class TestCaseExecutor {

	private static Logger logger = LoggerFactory.getLogger(TestCaseExecutor.class);
	Instructions instructionList;
	ExecutionDrivers driverList;
	String uuid;

	int cursor = 0;

	Set<Object> associatedBundleImplObjs = new HashSet<Object>();
	Set<String> bundleInterfaceSet = new HashSet<String>();
	Map<String, String> savedValues;

	boolean bOK;
	String executionResult;
	String executionMsg;
	ObjectNode massState = null;
	JsonNode massData = null;
	TaskRunner runner = null;
	String taskType;
	
	Object bundleObjHttpUtil = null;
	public static TestCaseExecutor testCase = null;
	
	public TestCaseExecutor(TaskRunner runner, String taskType, JsonNode taskData, UUID uuid, ObjectNode massObj) throws VMCJsonSchemaException, VMCLogicalException {
		this.runner = runner; 
		testCase = this;
		this.uuid = uuid.toString();
		this.massData = taskData;
		this.massState = massObj;
		// taskData
		ArrayNode drivers = (ArrayNode) taskData.withArray("drivers");
		if ("json".equalsIgnoreCase(taskType)) {
			driverList = new ExecutionDrivers(drivers);
			instructionList = new Instructions(MOObjectNode.copyNode(taskData), this);
			ObjectNode driverJson = driverList.asJSON();
			JsonNodeFactory.instance.arrayNode();
			driverJson.set("initialTypes", instructionTypes());
			this.massState.set("driver", driverJson);
			this.taskType = "json";
		} else if ("jmeter".equalsIgnoreCase(taskType)){
			instructionList = new Instructions(MOObjectNode.copyNode(taskData), this);
			this.taskType = "jmeter";
		}
	}

	public ExecutionResult execute() throws VMCLogicalException {
		ExecutionResult cr = new ExecutionResult(true, 0, "执行成功.");

		analyzeBundles();
		setupExecution();
		ExecutionResult newCr = doExecution();
		teardownExecution();

		return null == newCr ? cr : newCr;
	}
	
	public ExecutionResult executePressTest() {
		ExecutionResult cr = new ExecutionResult(true, 0, "压测执行成功.");
		analyzePTBundles();
		setupPTExecution();
		ExecutionResult newCr = doPTExecution();
		teardownPTExecution();

		return null == newCr ? cr : newCr;
	}

	public boolean resultOK() {
		return bOK;
	}
	
	public Set<String> bundleInterfaceSet(){
		return bundleInterfaceSet;
	}

	protected void analyzePTBundles() {
		containersClear();
		bundleInterfaceSet.add("IStressTest");
		if (runOnLine()) {
			bundleInterfaceSet.add("IHttpUtil");
			bundleInterfaceSet.add("IFileUtil");
			bundleInterfaceSet.add("IFileService");
		}
		getBundleObjs();
	}
	
	protected void analyzeBundles() {
		containersClear();
		collectBundlesName();
		if (runOnLine()) {
			bundleInterfaceSet.add("IHttpUtil");
			bundleInterfaceSet.add("IFileUtil");
			bundleInterfaceSet.add("IFileService");
		}
		getBundleObjs();
	}

	protected void setupPTExecution() {
		for (Object bdObj : associatedBundleImplObjs) {
			System.out.println("压力测试bundle obj:" + bdObj);
			logger.info("压力测试bundle obj:" + bdObj);
			OSGiUtil.teardownExecute(bdObj);
			if (null != massState)
				OSGiUtil.setupExecute(bdObj, massState.toString());
		}
		//TODO if not all api instruction,then throw exception.
		instructionList.allAPIInstruction();
		
		SGLogger.setHttpBundleObj(OSGiUtil.getOSGiService("IHttpUtil"));
		SGLogger.setStandSingleton(runner.vars.standSingleton());
	}
	
	protected void setupExecution() {
		// bundles
		// TODO clear bundle
		//
		for (Object bdObj : associatedBundleImplObjs) {
			OSGiUtil.teardownExecute(bdObj);

			if (null != massState) {
				System.err.println("TestCaseExecution.setupExecution:" + massState.toString());
				OSGiUtil.setupExecute(bdObj, massState.toString());
			}
		}
		
		// TODO init动作应该是类型无关的，这里为了保证确实会使用相关db，使用条件语句保护init动作
		// TODO 将setup中的特定bundle的特定初始化动作转移到massState中的driver字段下，以在attachData中初始化
//		driverList.setup(getInitialTypes());
		
		if (taskType.equalsIgnoreCase("json"))
			for (Instruction ins : instructionList.instructions) {
				// TODO instruction option
				if (null == ins || null == ins.action) {
					MOCommonUtil.sleepMilliSeconds(100);
				}
				if (ins.action.equalsIgnoreCase("click")) {
					MOCommonUtil.sleepMilliSeconds(300);
				}
	
				ins.beforeExecution();
			}
		
		SGLogger.setHttpBundleObj(OSGiUtil.getOSGiService("IHttpUtil"));
		SGLogger.setStandSingleton(runner.vars.standSingleton());
	}

	private ArrayNode instructionTypes() {
		ArrayNode instructionTypes = JsonNodeFactory.instance.arrayNode();
		Set<String> types = getInitialTypes();
		for(String type : types) {
			instructionTypes.add(type);
		}
		return instructionTypes;
	}
	/**
	 *  需要初始化的语句和对应的driver一致时，才需要真的去做对应的初始化
	 * @return
	 */
	private Set<String> getInitialTypes() {
		Set<String> initialTypes = new HashSet<String>();
		for (Instruction ins : instructionList.instructions()) {
			initialTypes.add(ins.driverType);
		}
		
//		if (instructionList.hasSQLInstruction() && driverList.hasJDBCDriver()) {
//			initialTypes.add("SQL");
//		}
		
//		if (instructionList.hasRedisInstruction() && driverList.hasRedisDriver()) {
//			initialTypes.add("Redis");
//		}
//		if (instructionList.hasRedisInstruction() && driverList.hasRedisDriver()) {
//			initialTypes.add("Redis");
//		}
		return initialTypes;
	}
	
	protected void doMsgBroadCase(String funcName, String info) {
		for (Object bdObj : associatedBundleImplObjs) {
			//TODO 此处采用观察者模式较佳(消息类型+数据进行广播的形式),暂时采用for循环(即本地维护受众列表的形式)
			if (null != funcName && null != info)
				OSGiUtil.doFunctionCall(bdObj, funcName, info);
		}
	}

	protected ExecutionResult doPTExecution() {
		ExecutionResult er = null;
		if ("jmeter".equalsIgnoreCase(taskType)) {
			Object jmxObj = OSGiUtil.getOSGiService("IStressTest");
			List<String> paramsInOut = new ArrayList<String>();
			Object result = OSGiUtil.doFunctionCall(jmxObj, "doTestProcess", massData.toString(),  paramsInOut);
			
			if (null == result) {
				er = new ExecutionResult(runner.vars.langMgr.langConst().executionFailWithNull());
//				er = new ExecutionResult("执行失败，返回空值。");
			} else {
				er = ExecutionResult.fromObject(result);
			}
		}
		
		return er;
	}
	
	protected ExecutionResult doExecution() throws VMCLogicalException {
		Instruction lastInstruction = null;
		ExecutionResult executionResult = null;
		if ("json".equalsIgnoreCase(taskType)) {
			int insIndex = 0;
			for (Instruction ins : instructionList.instructions) {
				// TODO instruction option
				if (ins.action.equalsIgnoreCase("click")) {
					MOCommonUtil.sleepMilliSeconds(300);
				}

				executionResult = ins.doExecution(insIndex);
				lastInstruction = ins;

				//FALSE时已经发送结束状态至上端系统
				if (!executionResult.bOK())
					break;
				++insIndex;
			}

			if (null != lastInstruction) {
				String finishInsRun = (String) lastInstruction.finishInstructionRun();
				System.err.println("[finishInsRun][]:" + finishInsRun);
				logger.info("[finishInsRun][]:" + finishInsRun);

				String info = String.join(",", bundleInterfaceSet.toArray(new String[0]));
				Object updateRun = SGLogger.updateRun(info);
				String updateRunMsg = String.format("[updateRun][%s]:%s", info, updateRun);
				System.err.println(updateRunMsg);
				logger.info(updateRunMsg);
			}
		}
		return executionResult;
	}

	protected void teardownPTExecution() {
		for (Object bdObj : associatedBundleImplObjs) {
			OSGiUtil.teardownExecute(bdObj);
		}
	}
	
	protected void teardownExecution() {
		// TODO
		for (Object bdObj : associatedBundleImplObjs) {
			OSGiUtil.teardownExecute(bdObj);
		}
//		driverList.clear(getInitialTypes());
	}

	private void containersClear() {
		associatedBundleImplObjs.clear();
		bundleInterfaceSet.clear();
	}
	
	private void getBundleObjs() {
		for (String bundleName : bundleInterfaceSet) {
			Object bdObj = OSGiUtil.getOSGiService(bundleName);
			if (null != bdObj) {
				associatedBundleImplObjs.add(bdObj);
			}
		}
	}
	
	private boolean runOnLine() {
		return null != massState && false == massState.get("standSingleton").asBoolean();
	}

	private void collectBundlesName() {
		for (Instruction instruction : instructionList.instructions) {
			String[] bundles = instruction.neededBundles();
			for (String bundle : bundles) {
				bundleInterfaceSet.add(bundle);
			}
		}
	}
}
