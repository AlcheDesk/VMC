package com.meowlomo.vmc.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
	
	static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);
	
	@Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(20);
        threadPoolTaskScheduler.setThreadNamePrefix("VMC-Async-T");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
	
	@Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new VMCAsyncExceptionHandler();
    }

    class VMCAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
		@Override
		public void handleUncaughtException(Throwable ex, Method method, Object... params) {
			logger.info("Async method: {} has uncaught exception,params:{}", method.getName(), params.toString());

	        if (ex instanceof Exception) {
	        	Exception asyncException = (Exception) ex;
	            logger.info("Exception in async method processTask:{}", asyncException.getMessage());
	        }
	        logger.info("Exception occured in executing task and uncaughted:");
	        ex.printStackTrace();
	        
	        //TODO task 状态回传,由于processTask无法保护其执行堆栈不被异常跳出,此处需要维护task在系统中的状态机
	        
//	        Run[] runs = {new Run()};
//			JsonNode receivedRun = task.getData();
//			runs[0].setId(receivedRun.get("id").asLong());
//			runs[0].setStatus("ERROR");
//			runs[0].setLog(runResult.msg());
//			
//			String tmpMsg = serverCommunicator.directFinishRun(runs);
		}
    }
}
