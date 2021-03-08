package com.meowlomo.vmc.config;

public class LangConstStringEN implements LangConstString{
	
	String _indexFinishErr = "Http response is null while finishing the instruction with index:%d, the system may be updating or network doesn't work!";
	String _indexAddErr = "Http response is null while adding the instruction with index:%d, the system may be updating or network doesn't work!";
	String _insBegin = "Instruction index %s, begin[%s], action[%s]";
	String _executionFail = "Execution fail";
	String _executionFailWithNull = "Exectuion fail, return null.";
	String _executionPass = "";
	String _stepOver = "Skip directly";
	String _OK = "Execution succeed";
	String _pressFail = "Stress test failed. The return value is empty";
	
	String _ignorePass = " Instruction [%s] is in ignore state,input:%s,skip directly!";
	String _nullResult = " Instruction [%s] input:%s ,result is null !!!";
	String _nullResultIgnore = " Instruction [%s] input:%s ,unrecognized result! Skip for ignore option!";
	String _unRecognizeResult = " Instruction [%s] input:%s ,unrecognized result!";
	String _ignoreReasonPass = "Skip for the ignore option!";
	
	String _normal3 = "Instruction[%s],input:%s,result:%s";
	String _normal3Target = "Instruction[%s],target[%s],input:%s,result:%s";
	
	String _error4 = "Instruction[%s],input:%s,exit code:%d, result:%s";
	String _error4Target = "Instrution[%s],target[%s],input:%s,exit code:%d, result:%s";
	
	@Override
	public String indexFinishErr(int index) {
		// TODO Auto-generated method stub
		return String.format(_indexFinishErr, index);
	}
	
	@Override
	public String indexAddErr(int index) {
		// TODO Auto-generated method stub
		return String.format(_indexAddErr, index);
	}
	
	@Override
	public String insBegin() {
		return _insBegin;
	}

	@Override
	public String executionFail() {
		// TODO Auto-generated method stub
		return _executionFail;
	}

	@Override
	public String executionFailWithNull() {
		// TODO Auto-generated method stub
		return _executionFailWithNull;
	}

	@Override
	public String executionPass() {
		// TODO Auto-generated method stub
		return _executionPass;
	}

	@Override
	public String stepOver() {
		// TODO Auto-generated method stub
		return _stepOver;
	}
	
	@Override
	public String stepOK() {
		return _OK;
	}
	
	@Override
	public String pressFail() {
		return _pressFail;
	}
	
	@Override
	public String ignorePass(String target, String input) {
		return String.format(_ignorePass, target, input);
	}
	
	@Override
	public String nullResult(String target, String input) {
		return String.format(_nullResult, target, input);
	}
	
	@Override
	public String nullResultIgnore(String target, String input) {
		return String.format(_nullResultIgnore, target, input);
	}
	
	@Override
	public String unRecognizeResult(String target, String input) {
		return String.format(_unRecognizeResult, target, input);
	}
	
	@Override
	public String ignoreReasonPass() {
		return _ignoreReasonPass;
	}
	
	@Override
	public String insNormal3() {
		return _normal3;
	}
	
	@Override
	public String insNormal3Target() {
		return _normal3Target;
	}
	
	@Override
	public String insError4() {
		return _error4;
	}
	
	@Override
	public String insError4Target() {
		return _error4Target;
	}
}
