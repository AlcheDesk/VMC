package com.meowlomo.vmc.abandoned;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ExecutorConfig {
	@Value("${meowlomo.task.worker.corepoolsize:10}")
	private int corePoolSize;
	
	@Value("${meowlomo.task.worker.maxpoolsize:200}")
	private int maxPoolSize;
	
	@Value("${meowlomo.task.worker.queuecapacity:20}")
	private int queueCapacity;
	
	@Value("${meowlomo.task.worker.binteractiveenabled:false}")
	private boolean bInteractiveEnabled;
	
	@Bean  
    public Executor taskWorkerExecutor() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("TaskWorkerExecutor - ");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  
        executor.initialize();
        return executor;
    }
}
