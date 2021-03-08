package com.meowlomo.vmc.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.abandoned.DataSourceEvent;
import com.meowlomo.vmc.abandoned.TaskReceivedEvent;
import com.meowlomo.vmc.model.Task;

@Component
public class VMCEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher publisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
	
	//TODO can be deleted when you see this, and the classes.
	
//	public void publish(Task task){
//		TaskReceivedEvent event = new TaskReceivedEvent(task);
//		publisher.publishEvent(event);
//	}
//	
//	public void publishDataSource(String info){
//		DataSourceEvent event = new DataSourceEvent(info);
//		publisher.publishEvent(event);
//	}
	
	public void publishRegister(){
		publisher.publishEvent(new RegisterEvent());
	}
}
