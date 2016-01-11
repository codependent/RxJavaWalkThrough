package com.codependent.rx.samplescada.sensor.impl;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;

public class FakeJamMachineBeltPositionSensor extends PositionSensor implements Observer<FakeSignal>{

	protected Double objectPosition;
	protected Double sensorPosition;
	protected Double beltSpeed;
	private Boolean beltStarted;
	
	public FakeJamMachineBeltPositionSensor(Double sensorPosition, Double objectStartingPosition, Double beltSpeed) {
		super(sensorPosition);
		this.objectPosition = objectStartingPosition;
		this.beltSpeed = beltSpeed;
		Observable<Signal> obs = Observable.<Signal>create( (s) -> {
			logger.info("create()");
			logger.info("objectPosition {}", objectPosition);
			logger.info("sensorPosition {}", super.sensorPosition);
			while(beltStarted){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				objectPosition += beltSpeed;
				logger.info("objectPosition2 {}", objectPosition);
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

	@Override
	public void onNext(FakeSignal s) {
		if(s.getType() == FakeSignal.Type.BELT_STARTED){
			beltStarted = true;
			observable.connect();
		}else if(s.getType() == FakeSignal.Type.BELT_STOPPED){
			beltStarted = false;
		}
	}
	
	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
	}

}
