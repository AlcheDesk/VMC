package com.meowlomo.vmc.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "meowlomo.bundles")
public class BundleInfo {
	Map<String, String> version;
	
	public Map<String, String> getVersion(){
		return version;
	}
	
	public void setVersion(Map<String, String> ver){
		version = ver;
	}
}
