package com.meowlomo.vmc.task;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.exception.VMCJsonSchemaException;
import com.meowlomo.vmc.util.MOObjectNode;

public class Instructions {

	protected List<Instruction> instructions = new LinkedList<Instruction>();
	protected int runId;
	protected int testCaseId;
	protected String testCaseName;
	protected String executionType;			//Production or Dev
	protected String executionGroup;		//VMC group , like CI ,DEV...
//	protected String tags;
	protected int timeout;
	protected TestCaseExecutor selfExecutor;
	protected boolean hasSqlInstruction = false;
	protected boolean hasRedisInstruction = false;
	protected boolean allAPIInstruction = true;
	
	public Instructions(MOObjectNode task, TestCaseExecutor executor) throws VMCJsonSchemaException {
		selfExecutor = executor;
		genInstructions(task);
		
		runId = task.get("id").asInt();
		testCaseId = task.get("testCaseId").asInt();
		testCaseName = task.get("name").asText();
		executionType = task.get("type").asText();
		executionGroup = task.get("group").asText();
//		tags = task.get("testCase").get("tags").asText("");
		timeout = task.get("testCase").get("timeout").asInt(60);
	}
	
	public List<Instruction> instructions(){
		return instructions;
	}
	
	public boolean hasSQLInstruction() {
		return hasSqlInstruction;
	}
	
	public boolean hasRedisInstruction() {
		return hasRedisInstruction;
	}
	
	public boolean allAPIInstruction() {
		return allAPIInstruction;
	}
	
	protected void genInstructions(ObjectNode task) throws VMCJsonSchemaException {
		ArrayNode instructionArray = (ArrayNode)task.get("testCase").withArray("instructions");
		for(JsonNode instructionNode : instructionArray){
			Instruction insObj = new Instruction(MOObjectNode.copyNode(instructionNode), this);
			if (allAPIInstruction && !insObj.apiType()) {
				allAPIInstruction = false;
			}
			if (!hasSqlInstruction && insObj.sqlType()) {
				hasSqlInstruction = true;
			}
			if (!hasRedisInstruction && insObj.redisType()) {
				hasRedisInstruction = true;
			}
			instructions.add(insObj);
		}
	}
	
	protected void doMsgBroadCase(String funcName, String info) {
		selfExecutor.doMsgBroadCase(funcName, info);
	}
	
	@Override
	public String toString() {
		return instructions.toString();
	}
}
