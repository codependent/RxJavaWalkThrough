package com.codependent.rx.samplescada.machine;

import rx.observables.ConnectableObservable;

import com.codependent.rx.samplescada.machine.sensor.Signal;



public abstract class JamMachine extends Machine{

	protected ConnectableObservable<Signal> observable;
	protected boolean filling;
	
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
