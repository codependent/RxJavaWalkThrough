package com.codependent.rx.samplescada.machine.impl;

import java.util.List;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.Belt;
import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;
import com.codependent.rx.samplescada.sensor.impl.FakeBeltPositionSensor;

public class FakeBelt extends Belt{

	protected Double objectPosition = 0.0;
	protected ConnectableObservable<Signal> observable;
	
	public FakeBelt(double length, double speed, List<PositionSensor> positionSensors) {
		super(length, speed, positionSensors);
	}

	@Override
	public void doOnBeltStart() {
		logger.info("doOnBeltStart() - objectPosition {}", objectPosition);
		Observable<Signal> observable = Observable.<Signal>create( (s) -> {
			boolean completed = false;
			while(!completed && state == State.STARTED){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (PositionSensor sensor : positionSensors) {
					FakeBeltPositionSensor fSensor = (FakeBeltPositionSensor)sensor;
					if(state == State.STARTED && objectPosition >= fSensor.getRange()[0] && objectPosition <= fSensor.getRange()[1]){
						objectPosition += speed;
						logger.info("objectPosition {} {}", objectPosition, new Object[]{fSensor.getRange()});
						if(objectPosition.doubleValue() >= fSensor.getRange()[1].doubleValue()){
							s.onNext(fSensor.getWatchedSignal());
							s.onCompleted();
							completed = true;
							break;
						}
					}
				}
			}
		}).subscribeOn(Schedulers.io());
		
		for (PositionSensor sensor : positionSensors) {
			if(sensor instanceof FakeBeltPositionSensor){
				FakeBeltPositionSensor fSensor = (FakeBeltPositionSensor)sensor;
				if(objectPosition >= fSensor.getRange()[0] && objectPosition <= fSensor.getRange()[1]){
					observable.subscribe((FakeBeltPositionSensor)sensor);
				}
			}
		}
	}

	@Override
	public void doOnBeltStop() {
		
	}

	@Override
	public void addEmptyJar() {
		logger.info("Adding empty jar");
		if(isEmptyBelt()){
			objectPosition = 0.0;
			emptyBelt = false;
		}
		
	}

	@Override
	public void removeFullJar() {
		logger.info("Removing full jar");
		if(!isEmptyBelt()){
			emptyBelt = true;
		}
	}

}
