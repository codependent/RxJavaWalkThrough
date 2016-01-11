package com.codependent.rx.samplescada.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class Machine{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public enum State { STARTED, STOPPED};
	
	protected State state = State.STOPPED;
	
	public void start(){
		logger.info("Starting");
		this.state = State.STARTED;
		doOnStart();
	}
	
	public void stop(){
		logger.info("Stopping");
		this.state = State.STOPPED;
		doOnStop();
	}
	
	public State getState() {
		return state;
	}
	
	public abstract void doOnStart();
	
	public abstract void doOnStop();
	
}
