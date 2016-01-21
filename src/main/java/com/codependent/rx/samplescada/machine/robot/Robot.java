package com.codependent.rx.samplescada.machine.robot;

import rx.observables.ConnectableObservable;

import com.codependent.rx.samplescada.machine.Machine;
import com.codependent.rx.samplescada.machine.Signal;

public abstract class Robot extends Machine{

	protected ConnectableObservable<Signal> observable;
	
	
	public Robot(String id) {
		super(id);
	}
	
	@Override
	protected void doOnStart() {
		observable.connect();
	}

	@Override
	protected void doOnStop() {
		
	}
	
	public ConnectableObservable<Signal> getObservable() {
		return observable;
	}
	
}
