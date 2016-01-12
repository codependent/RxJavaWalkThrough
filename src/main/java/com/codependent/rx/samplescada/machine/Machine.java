package com.codependent.rx.samplescada.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class Machine{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public enum State { STARTED, STOPPED, OPERATING, PAUSED};
	
	protected State state = State.STOPPED;
	
	public void start(){
		logger.info("Starting machine");
		this.state = State.STARTED;
		doOnStart();
	}
	
	public void stop(){
		logger.info("Stopping machine");
		this.state = State.STOPPED;
		doOnStop();
	}
	
	public void startOperating(){
		logger.info("Starting machine operation");
		this.state = State.OPERATING;
		doOnStartOperating();
	}
	
	public void stopOperating(){
		logger.info("Pausing machine operation");
		this.state = State.PAUSED;
		doOnStopOperating();
	}
	
	public State getState() {
		return state;
	}
	
	public abstract void doOnStart();
	
	public abstract void doOnStop();
	
	public abstract void doOnStartOperating();
	
	public abstract void doOnStopOperating();
	
}
