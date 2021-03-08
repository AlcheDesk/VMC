package com.meowlomo.vmc.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.exception.VMCJsonSchemaException;
import com.meowlomo.vmc.exception.VMCLogicalException;
import com.meowlomo.vmc.osgi.OSGiUtil;
import com.meowlomo.vmc.util.JsonNodeUtils;
import com.meowlomo.vmc.util.MOJsonUtil;

public class Instruction {

	final static Logger logger = LoggerFactory.getLogger(Instruction.class);
	protected ElementItem element;

	protected String id;
	protected String input;
	protected String instructionType; 			// webbrowser  ,webfunction, SQL
	protected String driverType; 				// webfunction, JDBC
	protected boolean isDriver;					//element 是否driver
	protected String action;
	protected String index; 					// logicalOrderIndex
	protected String options;

	protected String testCaseId;
	protected String elementId;		//过去的Object
	protected String target;
	protected boolean isOverwrite;	//是否覆盖，新加，用于instructionResult创建，只有为true时才传值
	
	protected String uuid;
	protected JsonNode extraData = JsonNodeFactory.instance.objectNode();

	protected Object bundleHttpUtil;
	private Instructions selfContainer;
	//TODO
	List<String> paramsInOut = new ArrayList<String>();
	
	RuntimeVariables vars;
	
	protected static Map<String, String[]> typeBundlesMap = new HashMap<String, String[]>(){
		private static final long serialVersionUID = 8260828595896715436L;
		{
			put("WebFunction", 				new String[] { "IWebDriver" });
			put("VirtualWebFunction", 		get("WebFunction"));	//to make a same object of array,where we may compare them.
			put("WebBrowser", 				get("WebFunction"));
			put("VirtualWebBrowser", 		get("WebFunction"));
			put("JavaScript", 				get("WebFunction"));
			put("SQL", 						new String[] { "IDataSource" });
			put("REST_API", 				new String[] { "IWebApiTest" });
			put("StringDataProcessor", 		new String[] { "IExpression" });
			put("MathExpressionProcessor", 	new String[] { "IExpression" });
			put("Redis",					new String[] { "IRedis"});
			put("SOAP_API",					new String[] { "IWebApiTest"});
			put("RPC_Dubbo",				new String[] { "IWebApiTest"});
			put("StringUtil",               new String[] { "IStringUtil"});
			put("CheckEmail",			    new String[] { "ICheckEmail"});
		}
	};
	
	protected static String[] apiRequiredFields = {"queryParameters", "requestHeaders"};
	protected static String[] emptyBundles = new String[0];
	
	public Instruction(ObjectNode instructionNode, Instructions container) throws VMCJsonSchemaException {
		id = instructionNode.get("id").asText();
		input = instructionNode.has("input") ? instructionNode.get("input").asText() : "";
		instructionType = instructionNode.get("type").asText();
		index = instructionNode.get("logicalOrderIndex").asText();
		if (instructionNode.has("isOverwrite") && instructionNode.get("isOverwrite").isBoolean())
			isOverwrite = instructionNode.get("isOverwrite").asBoolean();
		
		options = JsonNodeUtils.genOptionsStr(instructionNode);
		testCaseId = instructionNode.get("testCaseId").asText();
		selfContainer = container;
		vars = selfContainer.selfExecutor.runner.vars;
		action = MOJsonUtil.getJsonValue(instructionNode, "action", "elementAction", "Execute", "");
		
		if (InstructionUtils.rpcDubboType(instructionType.toLowerCase())) {
			target = instructionNode.has("target") ? instructionNode.get("target").asText() : "";
			driverType = "ZooKeeper";
			isDriver = instructionNode.get("isDriver").asBoolean();
			extraData = instructionNode.get("data");
		}
		else if (InstructionUtils.emailType(instructionType.toLowerCase())) {
			target = instructionNode.has("target") ? instructionNode.get("target").asText() : "";
			driverType = "Email";//instructionNode.getString("driverType");
			isDriver = instructionNode.get("isDriver").asBoolean();
			extraData = instructionNode.get("data");
			ExecutionDriver driver = TestCaseExecutor.testCase.driverList.getDriverByDriverType(driverType);
			target = driver.name;
		}
		else if (!InstructionUtils.expressionType(instructionType.toLowerCase()) && !InstructionUtils.utilType(instructionType.toLowerCase())) {
			elementId = instructionNode.get("elementId").asText();
			target = instructionNode.get("target").asText();
			JsonNode elementNode = instructionNode.get("element");
			
			//TODO 下两条属性不再从elementNode中取值
			if (instructionNode.has("driverType"))
				driverType = instructionNode.get("driverType").asText();
			else {
				driverType = elementNode.get("type").asText();
			}
			
			isDriver = instructionNode.get("isDriver").asBoolean();
			extraData = instructionNode.get("data");
			if (driverType.equals("API") || InstructionUtils.apiType(instructionType)) {
				for(String field : apiRequiredFields) {
					if (!extraData.has(field)) {
						((ObjectNode)extraData).set(field, JsonNodeFactory.instance.objectNode());
					} else {
						//如果字段值为 [] 型空值
						JsonNode value = extraData.get(field);
						if (value.isArray() && !value.isObject()) {
							((ObjectNode)extraData).set(field, JsonNodeFactory.instance.objectNode());
						}
					}
				}
			}
			element = ElementItem.genElementItem(isDriver, elementId, driverType, elementNode);
		} else {
			driverType = target = action = "Expression";
		}
	}

	String[] neededBundles() {
		// SOAP_API
		if (typeBundlesMap.containsKey(instructionType)) {
			return typeBundlesMap.get(instructionType);
		}
		return emptyBundles;
	}
	
	void beforeExecution() {
		bundleHttpUtil = OSGiUtil.getOSGiService("IHttpUtil");
	}

	public ExecutionResult doExecution(int insIndex) throws VMCLogicalException {
		//1.状态报告.结束之前的(若有的话)
		String finishInsRun = (String)finishInstructionRun(insIndex);
		System.err.println("[finishInsRun]:" + finishInsRun);
		if (null == finishInsRun){
			return new ExecutionResult(vars.langMgr.langConst().indexFinishErr(insIndex));
//			return new ExecutionResult("结束index为" + insIndex + "的语句的执行记录时返回null,系统有可能正在升级或者有网络问题!");
		}
		
		//2.状态报告.创建本InstructionRun
		long instructionRunId = addInstructionResult(index);
		if (0l == instructionRunId){
			System.err.println("[addInstructionResult]:" + index + " failed.");
			return new ExecutionResult(vars.langMgr.langConst().indexAddErr(insIndex));
//			return new ExecutionResult("添加index为" + index + "的语句的执行记录时返回null,系统有可能正在升级或者有网络问题!");
		}
		
		//3.关联runId到所有bundle(现在,是httpUtil)
		selfContainer.doMsgBroadCase("attachInstructionRunData", asString(instructionRunId));
//		SGLogger.instructionStart(String.format("第%s条,开始执行指令[%s],动作[%s]" ,index, target, action));
		SGLogger.instructionStart(String.format(vars.langMgr.langConst().insBegin() ,index, target, action));
		
		//4.执行一次Instruction
		// TODO Option,异常退出,截图/文件,状态报告等,全部都在bundle.step中?
		Object instructionResult = null;
		ExecutionResult stepExecutionResult = new ExecutionResult(vars.langMgr.langConst().executionFail());//执行失败
		InstructionOptions opts = new InstructionOptions(options);
		String inputStr = input.isEmpty() ? "(Empty)": input;
		if (opts.existOption(ContextConstant.INSTRUCTION_IGNORE)) {
//			SGLogger.pass(" 指令 [" + target + "]处于忽略状态,输入:" + inputStr + ",直接跳过!");
			SGLogger.pass(vars.langMgr.langConst().ignorePass(target, inputStr));
			stepExecutionResult = new ExecutionResult(true, vars.langMgr.langConst().stepOver());//"直接跳过!"
		} else {
			instructionResult = stepInstruction(instructionRunId);
			if (null == instructionResult) {
//				SGLogger.faild(" 指令 [" + target + "] 输入:" + inputStr + " :,结果为null !!!");
				SGLogger.faild(vars.langMgr.langConst().nullResult(target, inputStr));
			}else {
				stepExecutionResult = ExecutionResult.fromObject(instructionResult);
				if (null == stepExecutionResult) {
					if (opts.existOption(ContextConstant.RESULT_IGNORE)) {
						SGLogger.pass(vars.langMgr.langConst().nullResultIgnore(target, inputStr));
//						SGLogger.pass(" 指令 [" + target + "] 输入:" + inputStr + " :,结果无法识别! 由于设置有结果忽略指令,跳过!");
					} else {
						SGLogger.pass(vars.langMgr.langConst().unRecognizeResult(target, inputStr));
//						SGLogger.faild(" 指令 [" + target + "] 输入:" + inputStr + " :,结果无法识别!");
					}
				} else if (stepExecutionResult.bOK()) {
					SGLogger.pass(stepExecutionResult.passMsg(vars.langMgr.langConst(), index, target, inputStr));
				} else {
					if (opts.existOption(ContextConstant.RESULT_IGNORE)) {
						stepExecutionResult.revertOK();
						
						//作一次instruction run update.将resultOverwritten字段置为1
						ObjectNode updateInsRun = JsonNodeFactory.instance.objectNode();
						updateInsRun.put("resultOverwritten", 1);
						Object updateResult = OSGiUtil.doFunctionCall(bundleHttpUtil, "updateInstructionResult", updateInsRun.toString());
						System.err.println("updateInstructionResult.resultOverwritten.1:" + updateResult.toString());
						
						SGLogger.pass(stepExecutionResult.errorMsg(vars.langMgr.langConst(), index, target, inputStr) + "! " + vars.langMgr.langConst().ignoreReasonPass());//"! 由于设置了结果忽略指令,跳过!"
					} else {
						SGLogger.faild(stepExecutionResult.errorMsg(vars.langMgr.langConst(), index, target, input));
					}
				}
			}
		}
		return stepExecutionResult;
	}

	private Object stepInstruction(long instructionRunId) throws VMCLogicalException {
		String[] bundlesName = neededBundles();
		if (0 == bundlesName.length)
			throw new VMCLogicalException(instructionType + " doesn't match any bundles.");
		Object neededBundleImplObj = OSGiUtil.getOSGiService(bundlesName[0]);
		if (null == neededBundleImplObj)
			throw new VMCLogicalException(String.format("Type %s need bundle:%s doesn't be founded", instructionType, bundlesName[0]));
		Object result = OSGiUtil.doFunctionCall(neededBundleImplObj, "step", asString(instructionRunId),  paramsInOut);
		System.out.println("stepInstruction.list:" + paramsInOut);
		System.out.println("stepInstruction.result:" + result);
		return result;
	}

	private long addInstructionResult(String instructionId) {
		if (vars.standSingleton())
			return Long.valueOf(instructionId);
		
		String addInsResult = (String)OSGiUtil.doFunctionCall(bundleHttpUtil, "addInstructionResult", instructionId);
		logger.info("addInstructionResult call result:" + addInsResult);
		if (null == addInsResult)
			return 0l;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode instructionRun = null;
		try {
			instructionRun = mapper.readTree(addInsResult);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == instructionRun || instructionRun.has("status")) {
			runError(String.format(
					"      添加Instruction Run返回Error,执行中止.testcase id:%d, test case name:%s"
							+ ", instruction:%s, action:%s, input %s",
					uuid, selfContainer.testCaseName, toString(), action, input));
			return 0l;
		}
		long instructionRunId = runResultId(instructionRun);
		return instructionRunId;
	}

	public Object finishInstructionRun(int insIndex) {
		if (0 == insIndex)
			return "First Instruction Executed.No finish api called.";
		
		if (vars.standSingleton())
			return null;
		
		return OSGiUtil.doFunctionCall(bundleHttpUtil, "finishInstructionResult", "");
	}
	
	public Object finishInstructionRun() {
		if (vars.standSingleton())
			return null;
		
		return OSGiUtil.doFunctionCall(bundleHttpUtil, "finishInstructionResult", "");
	}
	
	public String asString(long instructionRunId) {
		ObjectNode instructionToBundle = JsonNodeFactory.instance.objectNode();
		instructionToBundle.put("id", id);
		instructionToBundle.put("input", input);
		instructionToBundle.put("instructionType", instructionType);
		instructionToBundle.put("action", action);
		instructionToBundle.put("index", index);
		instructionToBundle.put("options", options);
		instructionToBundle.put("testCaseId", testCaseId);
		
		instructionToBundle.put("runId", selfContainer.runId);
		instructionToBundle.put("instructionRunId", instructionRunId);
		
		
		instructionToBundle.put("driverType", driverType);
		instructionToBundle.put("isDriver", isDriver);
		instructionToBundle.put("elementId", elementId);
		if (null !=element)
			instructionToBundle.set("element", element.asJson());
		else
			instructionToBundle.set("element", NullNode.instance);
		instructionToBundle.put("target", target);
		
		if (isOverwrite)
			instructionToBundle.put("overwrite", isOverwrite);
		instructionToBundle.set("extraData", extraData);
	
		String inString = instructionToBundle.toString();
		System.err.println("The Instruction data : " + inString);
		return inString;
	}
	
	public String asString() {
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		node.put("id", id);
		node.put("input", input);
		node.put("instructionType", instructionType);
		node.put("action", action);
		node.put("index", index);
		node.put("options", options);
		node.put("testCaseId", testCaseId);
		
		node.put("runId", selfContainer.runId);	
		
		node.put("driverType", driverType);
		node.put("isDriver", isDriver);
		node.put("elementId", elementId);
		if (null !=element)
			node.set("element", element.asJson());
		node.put("target", target);
		node.set("extraData", extraData);
	
		String inString = node.toString();
		System.err.println("The Instruction data : " + inString);
		return inString;
	}
	
	@Override
	public String toString() {
		return asString();
	}
	
	private boolean runError(String log) {
		//TODO
//		SGLogger.error(log);
		return false;
	}
	
	private long runResultId(JsonNode run) {
		return run.withArray("data").get(0).get("id").asLong();		
	}
	
	public boolean apiType() {
		if (driverType.equals("API") || instructionType.equals("REST_API")) {
			return true;
		}
		return false;
	}
	
	public boolean sqlType() {
		return null != driverType && driverType.equals("JDBC") && instructionType.equals("SQL");
	}
	
	public boolean redisType() {
		return driverType.equals("Redis") || instructionType.equals("Redis");
	}
}
