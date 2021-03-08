package com.meowlomo.vmc.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.scheduler.DataFetcher;

@Component
public class PreloadedDataListener {
    
    static final Logger logger = LoggerFactory.getLogger(PreloadedDataListener.class);
    
    @Autowired
    DataFetcher dataFetcher = new DataFetcher();
    
    @Autowired
    RuntimeVariables runtimeVariables = new RuntimeVariables();
        
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
    	if (runtimeVariables.standSingleton()){
    		logger.info("loading system preset data. direct return by standSingleton !!!");
    		return;
    	}
    	
        logger.info("loading system preset data.");
        
        //first time fetch 
        this.dataFetcher.fetchGroup();
        this.dataFetcher.fetchStatus();
        this.dataFetcher.fetchJobType();
        this.dataFetcher.fetchTaskType();
        this.dataFetcher.fetchOperatingSystem();
//        this.dataFetcher.fetchElementDriverMap(); No need

        logger.info("Status :" + runtimeVariables.getStatuses());
        logger.info("Operating System :" + runtimeVariables.getOperatingSystems());
        logger.info("Job Type :" + runtimeVariables.getJobTypes());
        logger.info("Task Type :" + runtimeVariables.getTaskTypes());
        logger.info("Groups :" + runtimeVariables.getGroups());
    }
}
