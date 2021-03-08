package com.meowlomo.vmc.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LangMgr {

	LangConstString _instance = null;
	
	@Value("${meowlomo.config.vmc.lang}")
	String localLangString;
	
	public LangConstString langConst() {
		if (null == _instance) {
			if (StringUtils.isAllEmpty(localLangString))
				return _instance = new LangConstStringCN();
			else if (localLangString.toLowerCase().contains("cn"))
				return _instance = new LangConstStringCN();
			else
				return _instance = new LangConstStringEN();
		}
		return _instance;
	}
}
