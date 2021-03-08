package com.meowlomo.vmc.config;

public interface LangConstString {
	String indexFinishErr(int index);
	String indexAddErr(int index);
	String insBegin();
	String executionFail();
	String executionFailWithNull();
	String executionPass();
	String stepOver();
	String stepOK();
	String pressFail();
	String ignorePass(String target, String input);
	String nullResult(String target, String input);
	String nullResultIgnore(String target, String input);
	String unRecognizeResult(String target, String input);
	String ignoreReasonPass();
	
	String insNormal3();
	String insNormal3Target();
	
	String insError4();
	String insError4Target();
}
