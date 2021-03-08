package com.meowlomo.vmc.abandoned;

import org.springframework.context.ApplicationEvent;

public class TaskReceivedEvent extends ApplicationEvent {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -5258080548672413760L;

	public TaskReceivedEvent(Object source) {
		super(source);
	}
}
