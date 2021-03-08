package com.meowlomo.vmc.scheduler;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.FileServiceApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.file.FileService;

@Component
public class AgentHealthChecker {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentHealthChecker.class);  
	
	@Autowired
	RuntimeVariables runtimeVariables;
	
	@Autowired
	FileServiceApi fileServiceApi;
	
	@Value("${meowlomo.config.vmc.filesystem.check.enable}")
	private boolean enableFileSystemCheck;
	
	public boolean isFileSystemOK(){
		logger.info("Checking file mounting point");
	
		if (!enableFileSystemCheck){
			logger.info("[Direct Return True By Config Enable False] Checking file mounting point");
			return true;
		}
		
		FileService fileService = fileServiceApi.createService();
		if(fileService.accessiable()){
            if(!fileService.isWritable(Paths.get("results"))){
                logger.error("file system has no WRITE permission");
                return true;
            }
			return true;
		}else{
			logger.error("File mounting point ["+runtimeVariables.getFileServerMountPoint()+"] is not a directory");
			return false;
		}
	}
}
