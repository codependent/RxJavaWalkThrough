package com.codependent.rx.samplescada.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.Signal.Type;



public abstract class Machine{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public enum State { STARTED, STOPPED, OPERATING, PAUSED};
	
	protected volatile State state = State.STOPPED;
	
	private String id;
	
	private Observable<Signal> lifecycleObservable;
	
	private volatile Signal lifeCycleSignal;
	
	public Machine(String id){
		this.id = id;
		lifecycleObservable = Observable.<Signal>create( (s) -> {
			while(true){
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(lifeCycleSignal!=null){
					s.onNext(lifeCycleSignal);
					lifeCycleSignal = null;
				}
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.io());
	}
	
	public void start(){
		logger.info("Starting machine");
		this.state = State.STARTED;
		this.lifeCycleSignal = new Signal(Type.MACHINE_STARTED, id);
		doOnStart();
	}
	
	public void stop(){
		logger.info("Stopping machine");
		this.state = State.STOPPED;
		this.lifeCycleSignal = new Signal(Type.MACHINE_STOPPED, id);
		doOnStop();
	}
	
	public void startOperating(){
		logger.info("Starting machine operation");
		this.state = State.OPERATING;
		this.lifeCycleSignal = new Signal(Type.MACHINE_OPERATING, id);
		doOnStartOperating();
	}
	
	public void stopOperating(){
		logger.info("Pausing machine operation");
		this.state = State.PAUSED;
		this.lifeCycleSignal = new Signal(Type.MACHINE_PAUSED, id);
		doOnStopOperating();
	}
	
	public State getState() {
		return state;
	}
	
	public Observable<Signal> getLifecycleObservable() {
		return lifecycleObservable;
	}
	
	public abstract void doOnStart();
	
	public abstract void doOnStop();
	
	public abstract void doOnStartOperating();
	
	public abstract void doOnStopOperating();
	
}
