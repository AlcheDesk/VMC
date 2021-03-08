package com.meowlomo.vmc.task;

public class ThreadCompleteListener {
	private boolean terminated = false;
	
	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}	
}
