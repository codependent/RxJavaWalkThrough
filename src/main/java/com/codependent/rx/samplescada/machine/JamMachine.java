package com.codependent.rx.samplescada.machine;

import rx.observables.ConnectableObservable;

import com.codependent.rx.samplescada.sensor.Signal;



public abstract class JamMachine extends Machine{

	protected ConnectableObservable<Signal> observable;
	
	@Override
	public void doOnStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doOnStop() {
		// TODO Auto-generated method stub
		
	}

	public ConnectableObservable<Signal> getObservable() {
		return observable;
	}
	
}
