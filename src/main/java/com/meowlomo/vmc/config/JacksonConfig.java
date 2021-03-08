package com.meowlomo.vmc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class JacksonConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(JacksonConfig.class); 

	public JacksonConfig() {
    		logger.info("jackson configuration class has been constructed.");
	}
	
    @Bean
    public JavaTimeModule registerJsonOrgModule() {

    	logger.info("registered Java time model for jackson.");
        return new JavaTimeModule();       
    } 
    
    @Bean
    public Jdk8Module registerJdk8Module() {
    	logger.info("jackson configuration class has been constructed.");
    	logger.info("registered jdk 8 model for jackson.");
        return new Jdk8Module();       
    } 
    
}
