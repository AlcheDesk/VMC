package com.meowlomo.vmc.task;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowlomo.vmc.config.LangConstString;

public class ExecutionResult{
	public ExecutionResult(boolean result, int code, String info) {
		bOK = result;
		exitCode = code;
		msg = info;
	}
	
	public ExecutionResult(boolean result, String trueInfo, String falseInfo) {
		bOK = result;
		if (bOK)
			msg = trueInfo;
		else
			msg = falseInfo;
	}
	
	public ExecutionResult(boolean result, String info) {
		bOK = result;
		msg = info;
	}
	
	public ExecutionResult(String info) {
		bOK = false;
		msg = info;
	}
	
	public ExecutionResult() {
		bOK = true;
		msg = "";
	}
	
	public boolean revertOK() {
		return bOK = !bOK;
	}

	public boolean bOK() {
		return bOK;
	}
	
	public String msg() {
		return msg;
	}
	
	public Integer exitCode() {
		return exitCode;
	}
	
	public static ExecutionResult fromString(String str) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode instructionRun = null;
		try {
			instructionRun = mapper.readTree(str);
			ExecutionResult er = new ExecutionResult();
			if (instructionRun.has("bOK") && instructionRun.get("bOK").isBoolean()) {
				er.bOK = instructionRun.get("bOK").asBoolean();
			}
			if (instructionRun.has("msg") && instructionRun.get("msg").isTextual()) {
				er.msg = instructionRun.get("msg").asText();
			}
			if (instructionRun.has("exitCode") && instructionRun.get("exitCode").canConvertToInt()) {
				er.exitCode = instructionRun.get("exitCode").asInt();
			}
			return er;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param obj	must be a string.Down type conversion will happen.
	 * @return
	 */
	public static ExecutionResult fromObject(Object obj) {
		String str = (String)obj;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode instructionRun = null;
		try {
			instructionRun = mapper.readTree(str);
			ExecutionResult er = new ExecutionResult();
			if (instructionRun.has("bOK") && instructionRun.get("bOK").isBoolean()) {
				er.bOK = instructionRun.get("bOK").asBoolean();
			}
			if (instructionRun.has("msg") && instructionRun.get("msg").isTextual()) {
				er.msg = instructionRun.get("msg").asText();
			}
			if (instructionRun.has("exitCode") && instructionRun.get("exitCode").canConvertToInt()) {
				er.exitCode = instructionRun.get("exitCode").asInt();
			}
			return er;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String passMsg(LangConstString lcs, String index, String target, String input) {
		if (null == target || target.isEmpty())
			return String.format(lcs.insNormal3(), index, input, msg);//"指令[%s],输入:%s,结果:%s"
			
		return String.format(lcs.insNormal3Target(), index, target, input, msg);//"指令[%s],目标[%s],输入:%s,结果:%s"
	}
	
	public String errorMsg(LangConstString lcs, String index, String target, String input) {
		if (StringUtils.isEmpty(target))
			return String.format(lcs.insError4(), index, input, exitCode, msg);//"指令[%s],输入:%s,退出码:%d, 结果:%s"
			
		return String.format(lcs.insError4Target(), index, target, input, exitCode, msg);//"指令[%s],目标[%s],输入:%s,退出码:%d, 结果:%s"
	}
	
	private boolean bOK;
	private String msg;
	private Integer exitCode;	// 辅助字段，一般场景不需要
}
