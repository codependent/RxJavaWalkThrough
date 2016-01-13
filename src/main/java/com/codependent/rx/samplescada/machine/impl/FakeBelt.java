package com.codependent.rx.samplescada.machine.impl;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.Belt;
import com.codependent.rx.samplescada.machine.Signal;
import com.codependent.rx.samplescada.machine.Signal.Type;
import com.codependent.rx.samplescada.machine.sensor.PositionSensor;
import com.codependent.rx.samplescada.machine.sensor.impl.FakeBeltPositionSensor;

public class FakeBelt extends Belt{

	protected Double objectPosition = 0.0;
	protected ConnectableObservable<Signal> observable;
	private Subscription subscription;
	
	public FakeBelt(String id, double length, double speed, List<PositionSensor> positionSensors) {
		super(id, length, speed, positionSensors);
	}

	@Override
	public void doOnBeltStart() {
		
	}

	@Override
	public void doOnBeltStop() {
		
	}
	
	@Override
	public void doOnBeltStartOperating() {
		logger.info("doOnBeltStartOperating() - objectPosition {}", objectPosition);
		Observable<Signal> observable = Observable.<Signal>create( (s) -> {
			boolean completed = false;
			while(!completed){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(state == State.OPERATING){
					logger.info("doOnBeltStartOperating() - objectPosition22 {}", objectPosition);
					for (PositionSensor sensor : positionSensors) {
						FakeBeltPositionSensor fSensor = (FakeBeltPositionSensor)sensor;
						if(state == State.OPERATING && objectPosition >= fSensor.getRange()[0] && objectPosition <= fSensor.getRange()[1]){
							logger.info("doOnBeltStartOperating() - objectPosition33 {}", objectPosition);
							objectPosition += speed;
							Signal signal = new Signal(Type.JAR_IN_BELT_POSITION, objectPosition+"");
							s.onNext(signal);
							logger.info("objectPosition {} {}", objectPosition, new Object[]{fSensor.getRange()});
							if(objectPosition.doubleValue() >= fSensor.getRange()[1].doubleValue()){
								s.onNext(fSensor.getWatchedSignal());
								s.onCompleted();
								completed = true;
								subscription = null;
								break;
							}
						}
					}
				}
			}
		})
		.subscribeOn(Schedulers.io());
		
		for (PositionSensor sensor : positionSensors) {
			if(sensor instanceof FakeBeltPositionSensor){
				FakeBeltPositionSensor fSensor = (FakeBeltPositionSensor)sensor;
				logger.info(" op{} 0-{} 1-{}", objectPosition, fSensor.getRange()[0], fSensor.getRange()[1]);
				if(subscription == null && objectPosition >= fSensor.getRange()[0] && objectPosition <= fSensor.getRange()[1]){
					subscription = observable.subscribe((FakeBeltPositionSensor)sensor);
				}
			}
		}
	}
	
	@Override
	public void doOnBeltStopOperating() {
		
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
