package com.meowlomo.vmc.task;

import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstructionOptions {
	
	Logger logger = LoggerFactory.getLogger(InstructionOptions.class);
	
//	private HashMap<String,Boolean> options = new HashMap<String,Boolean>();
	private HashSet<String> optionSet = new HashSet<String>();
	private Map<String, String> optionMap = new HashMap<String, String>();
	private Map<String, String> savedData = new HashMap<String, String>();
	
	public InstructionOptions(){
		
	}
	
	public InstructionOptions(String optionStr) {
		genFromOptionStr(optionStr);
	}

	public String getOptionsString() {
		//get the key Set
		Set<String> optionsKey = this.optionSet;//this.options.keySet();
		if(optionsKey.isEmpty()){
			return "";
		}
		
		String returnString = "";
		for(String key:optionsKey){
			returnString += String.format("{%s}", key);
		}
		return returnString;
	}
	
	public boolean notEmpty() {
		return count() > 0;
	}

	public int count(){
		return this.optionSet.size();
	}
	
	public Boolean addOption(String option){
		if (null != option && !option.isEmpty()){
			this.optionSet.add(option);
			return true;
		}
		return false;
	}
	
	public Boolean addOptionPair(String option, String value){
		if (null != value){
			if (addOption(option)){
				this.optionMap.put(option, value);
				return true;
			}
		}
		return false;
	}
	
	public String getValue(String option){
		String value = optionMap.get(option);
		return value;
	}
	
	public boolean existOption(String option){
		if (null == option || option.isEmpty())
			return false;
		return this.optionSet.contains(option);
	}
	
	public void clearOption(){
		this.optionSet.clear();
		this.optionSet = new HashSet<String>();
		this.optionMap.clear();
		this.optionMap = new HashMap<String, String>();
//		this.savedData = null;
	}
	
	public void resetSavedDatas() {
		savedData = new HashMap<String, String>();
	}
	
	public void printOptions(){
		logger.info(this.optionSet.toString());
	}
	
	public void attachSavedDatas(Map<String, String> savedDatas){
		this.savedData = savedDatas;
	}

	
	public Map<String, String> savedDatas(){
		return this.savedData;
	}
	
	public String replaceSavedData(String input){
		if (null == this.savedData || this.savedData.isEmpty())
			return input;
		else{
			return transferInput(input);
		}
	}
	
	private String transferInput(String input) {
		// TODO the converter should be a dependent bundle,for plugable.
		if (null == input || input.isEmpty())
			input = "";
		else {
			input = InstructionUtils.convertMultiFuncInput(input);
			input = InstructionUtils.convertSavedValues(input, savedData);
		}
		return input;
	}
	
	public boolean doInputSaveOptionWork(String input) {
		if (existOption(ContextConstant.OPTION_SAVE_INPUT)) {
			String savedKey = getValue(ContextConstant.OPTION_SAVE_INPUT);
			if (!savedKey.isEmpty()) {
				if (savedData.containsKey(savedKey)) {
					logger.info(String.format("[OPTION WARNING] {} will be replaced!", savedKey));
				}
				savedData.put(savedKey, input);
				return true;
			} else
				logger.error(String.format("[OPTION ERROR] '{}' will be ignored: for save key it is empty!",
						ContextConstant.OPTION_SAVE_INPUT));
		}
		return false;
	}
	
	public String replaceLocatorValue(String locatorValue) {
		if (existOption(ContextConstant.ELEMENT_REPLACE_LOCATOR_VALUE)){
			String value = getValue(ContextConstant.ELEMENT_REPLACE_LOCATOR_VALUE);
			if (null != value){
				//TODO 非常重要，隐式顺序
				value = replaceSavedData(value);
				String[] replaceValues = value.split(",");
				if (replaceValues.length > 20){
					SGLogger.faild("      [ELE_REPLACE_LOCATOR_VALUE] element more than 20 not support NOW.");
					return null;
				}
				for(int i = 0; i < replaceValues.length; ++i){
					String var = replaceValues[i];
					String matchVar = String.format("{%d}", i);
					locatorValue = locatorValue.replace(matchVar, var);
				}
			}
		}
		return locatorValue;
	}
	
	public int genFromOptionStr(String optionStr) {
		String[] optionArray = optionStr.split(";");
		for (int optionIndex = 0; optionIndex < optionArray.length; ++optionIndex) {
			String optionItem = optionArray[optionIndex];
			String[] optionItems = optionItem.split(":");
			if (2 == optionItems.length) {
				if (optionItems[0].trim().isEmpty() || optionItems[1].trim().isEmpty()) {
					SGLogger.faild(String.format("[option error] [%s] with empty value!", optionItem));
				}else {
					addOptionPair(optionItems[0], optionItems[1]);
				}
			} else if (1 == optionItems.length) {
				if (optionItem.endsWith(":")) {
					SGLogger.faild(String.format("[option error] [%s] with empty value!", optionItem));
				} else {
					addOption(optionItem);
				}
			}
		}
		return count();
	}
	
//	public String doTextSaveIfNeeded() {
//		int optionCount = count();
//		String textSaveKey = "";
//		if (optionCount > 0) {
//			textSaveKey = getTextSaveOptionKey(textSaveKey);
//			
//			if (!textSaveKey.isEmpty()) {
//				String elementText = ActionCommon.elementText();
//				if (savedData.containsKey(textSaveKey)) {
//					logger.error("[OPTION ERROR] {} will be replaced!", elementText);
//					System.err.println(String.format("[OPTION ERROR] %s will be replaced!", elementText));
//				}
//				savedData.put(textSaveKey, elementText);
//			}
//		}
//		
//		return textSaveKey;
//	}
	
	private String getTextSaveOptionKey(String elementTextKey) {
		if (existOption(ContextConstant.OPTION_SAVE_TEXT)) {
			String savedKey = getValue(ContextConstant.OPTION_SAVE_TEXT);
			if (!savedKey.isEmpty())
				elementTextKey = savedKey;
			else
				logger.error(String.format("[OPTION ERROR] '{}' will be ignored: for save key it is empty!",
						ContextConstant.OPTION_SAVE_TEXT));
		}
		
		return elementTextKey;
	}
	
	@Override
	public String toString() {
		String tmpSet = "option:" + optionSet.toString();
		String tmpHash = "option key-value pair:" + optionMap.toString();
		String tmpSave = "temp saved key-value pair:" + savedData.toString();
		return tmpSet + "\r\n" + tmpHash + "\r\n" + tmpSave;
	}
}
