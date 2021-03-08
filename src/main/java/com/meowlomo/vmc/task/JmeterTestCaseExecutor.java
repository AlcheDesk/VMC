package com.meowlomo.vmc.task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.osgi.OSGiUtil;

public class JmeterTestCaseExecutor extends TestCaseExecutor {
	List<String> paramsInOut = new ArrayList<String>();
	String taskJson;
	public JmeterTestCaseExecutor(TaskRunner runner, String taskType, JsonNode taskData, UUID uuid,
			ObjectNode massObj) throws Exception {
		super(runner, taskType, taskData, uuid, massObj);
		taskJson = taskData.toString();
	}
	
	public ExecutionResult execute() {
		ExecutionResult cr = new ExecutionResult(true, 0, "执行成功.");

		analyzeBundles();
		setupExecution();
		ExecutionResult newCr = doExecution();
		teardownExecution();

		return null == newCr ? cr : newCr;
	}

	protected void setupExecution() {
		for (Object bdObj : associatedBundleImplObjs) {
			OSGiUtil.teardownExecute(bdObj);
			if (null != massState)
				OSGiUtil.setupExecute(bdObj, massState.toString());
		}
		SGLogger.setHttpBundleObj(OSGiUtil.getOSGiService("IHttpUtil"));
		SGLogger.setStandSingleton(runner.vars.standSingleton());
	}
	
	protected ExecutionResult doExecution() {
		//找到压测bundle,把task直接扔进去
		ExecutionResult er = doBatchExecution();
		return er;
	}
	
	public ExecutionResult doBatchExecution() {
		//1.状态报告.结束之前的(若有的话)	
		//2.状态报告.创建本InstructionRun
		//3.关联runId到所有bundle(现在,是httpUtil)
//		SGLogger.instructionStart(String.format("第%s条,开始执行指令[%s],动作[%s]" ,index, target, action));
		//4.执行一次Instruction
		// TODO Option,异常退出,截图/文件,状态报告等,全部都在bundle.step中?
	
		ExecutionResult batchExecutionResult = null;
		Object jmeterBundle = OSGiUtil.getOSGiService("IStressTest");
		//TODO
		Object result = OSGiUtil.doFunctionCall(jmeterBundle, "doTestProcess", taskJson,  paramsInOut);
		System.out.println("doBatchExecution.list:" + paramsInOut);
		System.out.println("doBatchExecution.result:" + result);
		if (null == result) {
//			batchExecutionResult = new ExecutionResult("压测执行失败.返回值为空");
			batchExecutionResult = new ExecutionResult(runner.vars.langMgr.langConst().pressFail());
		} else {
			batchExecutionResult= ExecutionResult.fromObject(result);
		}
		return batchExecutionResult;
	}
}
