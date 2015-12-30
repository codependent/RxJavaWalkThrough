package com.codependent.rx.samplescada.sensor;

import rx.Observable;

public abstract class Sensor{

	protected Observable<Signal> observable;
	
	protected Sensor(Observable<Signal> observable){
		this.observable = observable;
	}

}
