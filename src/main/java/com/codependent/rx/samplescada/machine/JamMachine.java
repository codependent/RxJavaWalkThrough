package com.codependent.rx.samplescada.machine;

import rx.observables.ConnectableObservable;



public abstract class JamMachine extends Machine{

	protected ConnectableObservable<Signal> observable;
	protected boolean filling;
	
	public JamMachine(String id){
		super(id);
	}
	
	@Override
	public void doOnStart() {
	}

	@Override
	public void doOnStop() {
	}
	
	@Override
	public void doOnStartOperating() {
	}
	
	@Override
	public void doOnStopOperating() {
	}
	
	public ConnectableObservable<Signal> getObservable() {
		return observable;
	}
	
	public boolean isFilling() {
		return filling;
	}
	
}
