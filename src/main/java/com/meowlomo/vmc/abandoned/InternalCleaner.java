package com.meowlomo.vmc.abandoned;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.file.FileServerCommunicator;

@Component
@Deprecated
public class InternalCleaner  implements Runnable {
	static final Logger logger = LoggerFactory.getLogger(InternalCleaner.class);

	@Autowired
	RuntimeVariables runtimeVariables;
	
	@Override
	public void run() {
		/*clean the temp folder
		 * set the worker to paused first
		 */
		/*check until the worker is free 
		 * should set a
		 */
		do{
			if(runtimeVariables.freeStatus()){
				runtimeVariables.setFileCleanPaused(true);
				FileServerCommunicator.deleteTempFolder();
				runtimeVariables.setFileCleanPaused(false);
				break;
			}
		}while(!runtimeVariables.freeStatus());
	}
}
