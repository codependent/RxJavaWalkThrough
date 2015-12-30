package com.codependent.rx.samplescada.sensor.impl;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;

public class FakeJamMachineBeltPositionSensor extends PositionSensor{

	private Double objectPosition;
	protected Double sensorPosition;
	
	public FakeJamMachineBeltPositionSensor(Double sensorPosition) {
		super(sensorPosition);
	}
	
	public Double getSensorPosition() {
		return sensorPosition;
	}
	
	public void setObjectPosition(double objectPosition){
		this.objectPosition = objectPosition;
	}

	@Override
	public void doOnStart() {
		Observable<Signal> obs = Observable.<Signal>create( (s) -> {
			if(objectPosition != null && objectPosition == sensorPosition){
				s.onNext(new Signal(Signal.Type.JAR_IN_JARMACHINE));
				s.onCompleted();
			}
		}).subscribeOn(Schedulers.io());
		observable = obs.publish();
		observable.connect();
	}

	@Override
	public void doOnStop() {
		
	}

}
