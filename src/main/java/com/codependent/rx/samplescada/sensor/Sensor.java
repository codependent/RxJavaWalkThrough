package com.codependent.rx.samplescada.sensor;

import com.codependent.rx.samplescada.machine.Machine;

import rx.Observable;
import rx.observables.ConnectableObservable;

public abstract class Sensor extends Machine{

	protected ConnectableObservable<Signal> observable;
	
	protected Sensor(){
	}
	
	public Observable<Signal> getObservable() {
		return observable;
	}

}
