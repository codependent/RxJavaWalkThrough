package com.codependent.rx.samplescada.sensor.impl;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.Belt;
import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;

public class FakeJamMachineBeltPositionSensor extends PositionSensor{

	protected Double objectPosition;
	protected Double sensorPosition;
	
	public FakeJamMachineBeltPositionSensor(Belt belt, Double sensorPosition, Double objectStartingPosition) {
		super(belt, sensorPosition);
		this.objectPosition = objectStartingPosition;
		Observable<Signal> obs = Observable.<Signal>create( (s) -> {
			logger.info("FakeJamMachineBeltPositionSensor - create()");
			logger.info("FakeJamMachineBeltPositionSensor - objectPosition {}", objectPosition);
			logger.info("FakeJamMachineBeltPositionSensor - sensorPosition {}", super.sensorPosition);
			while(belt.getState() == State.STARTED ){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				objectPosition += belt.getSpeed();
				logger.info("FakeJamMachineBeltPositionSensor - objectPosition2 {}", objectPosition);
				if(objectPosition.doubleValue() == super.sensorPosition.doubleValue()){
					s.onNext(new Signal(Signal.Type.JAR_IN_JARMACHINE));
				}
			}
		}).subscribeOn(Schedulers.io());
		observable = obs.publish();
	}
	
	public Double getSensorPosition() {
		return sensorPosition;
	}
	
	public void setObjectPosition(double objectPosition){
		this.objectPosition = objectPosition;
	}

	@Override
	public void doOnStart(){
		logger.info("doOnStart");
	}

	@Override
	public void doOnStop() {
		logger.info("doOnStop");
	}

}
