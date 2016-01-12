package com.codependent.rx.samplescada.machine.sensor;

import rx.observables.ConnectableObservable;

import com.codependent.rx.samplescada.machine.Machine;

public abstract class Sensor extends Machine{

	protected ConnectableObservable<Signal> observable;
	
	protected Sensor(){
	}
	
	public ConnectableObservable<Signal> getObservable() {
		return observable;
	}

}
