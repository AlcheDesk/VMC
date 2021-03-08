package com.meowlomo.vmc.config;

public class LangConstStringCN implements LangConstString{

	String _indexFinishErr = "结束index为%d的语句的执行记录时返回null,系统有可能正在升级或者有网络问题!";
	String _indexAddErr = "添加index为%d的语句的执行记录时返回null,系统有可能正在升级或者有网络问题!";
	String _insBegin = "第%s条,开始执行指令[%s],动作[%s]";
	String _executionFail = "执行失败";
	String _executionFailWithNull = "执行失败，返回空值。";
	String _executionPass = "";
	String _stepOver = "直接跳过";
	String _OK = "执行成功";
	String _pressFail = "压测执行失败.返回值为空";
	
	String _ignorePass = " 指令 [%s]处于忽略状态,输入:%s,直接跳过!";
	String _nullResult = " 指令 [%s] 输入:%s ,结果为null !!!";
	String _nullResultIgnore = " 指令 [%s] 输入:%s ,结果无法识别! 由于设置有结果忽略指令,跳过!";
	String _unRecognizeResult = " 指令 [%s] 输入:%s ,结果无法识别!";
	String _ignoreReasonPass = "由于设置有结果忽略指令,跳过!";
	
	String _normal3 = "指令[%s],输入:%s,结果:%s";
	String _normal3Target = "指令[%s],目标[%s],输入:%s,结果:%s";
	
	String _error4 = "指令[%s],输入:%s,退出码:%d, 结果:%s";
	String _error4Target = "指令[%s],目标[%s],输入:%s,退出码:%d, 结果:%s";
	
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
