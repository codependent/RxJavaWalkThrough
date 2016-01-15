package com.codependent.rx.samplescada.machine;

import rx.observables.ConnectableObservable;



public abstract class JamMachine extends Machine{

	protected ConnectableObservable<Signal> observable;
	protected boolean filling;
	
	public JamMachine(String id){
		super(id);
	}
	
	@Override
	protected void doOnStart() {
	}

	@Override
	protected void doOnStop() {
	}
	
	@Override
	protected void doOnStartOperating() {
	}
	
	@Override
	protected void doOnStopOperating() {
	}
	
	public ConnectableObservable<Signal> getObservable() {
		return observable;
	}
	
	public boolean isFilling() {
		return filling;
	}
	
}
