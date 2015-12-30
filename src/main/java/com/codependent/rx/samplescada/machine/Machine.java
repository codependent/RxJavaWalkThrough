package com.codependent.rx.samplescada.machine;



public abstract class Machine{

	public enum State { STARTED, STOPPED};
	
	protected State state = State.STOPPED;
	
	public void start(){
		this.state = State.STARTED;
		doOnStart();
	}
	
	public void stop(){
		this.state = State.STOPPED;
		doOnStop();
	}
	
	public State getState() {
		return state;
	}
	
	public abstract void doOnStart();
	
	public abstract void doOnStop();
	
}
