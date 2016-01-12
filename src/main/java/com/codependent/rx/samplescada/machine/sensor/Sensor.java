package com.codependent.rx.samplescada.machine.sensor;

import rx.observables.ConnectableObservable;

import com.codependent.rx.samplescada.machine.Machine;
import com.codependent.rx.samplescada.machine.Signal;

public abstract class Sensor extends Machine{

	protected ConnectableObservable<Signal> observable;
	
	protected Sensor(String id){
		super(id);
	}
	
	public ConnectableObservable<Signal> getObservable() {
		return observable;
	}

}
