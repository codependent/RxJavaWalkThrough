package com.codependent.rx.samplescada.machine.sensor.impl;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.Signal;
import com.codependent.rx.samplescada.machine.sensor.PositionSensor;

public class FakeBeltPositionSensor extends PositionSensor implements Observer<Signal>{

	protected Double objectPosition;
	protected Double sensorPosition;
	protected Double[] range;
	protected Double beltSpeed;
	private Signal watchedSignal;
	private volatile Signal signal;
	
	public FakeBeltPositionSensor(String id, Double[] range, Double objectStartingPosition, Double beltSpeed, Signal watchedSignal) {
		super(id, range[1]);
		this.objectPosition = objectStartingPosition;
		this.beltSpeed = beltSpeed;
		this.watchedSignal = watchedSignal;
		this.range = range;
		
		observable = Observable.<Signal>create( (s) -> {
			while(state==State.STARTED){
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(signal!=null){
					logger.debug("{}",signal);
					s.onNext(signal);
					signal = null;
				}
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.io())
		.publish();
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
		observable.connect();
	}

	@Override
	public void doOnStop() {
		logger.info("doOnStop");
	}
	
	@Override
	public void doOnStartOperating() {
		logger.info("doOnStartOperating");
	}
	
	@Override
	public void doOnStopOperating() {
		logger.info("doOnStopOperating");
	}

	@Override
	public void onNext(Signal s) {
		signal = s;
	}
	
	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
	}
	
	public Double[] getRange() {
		return range;
	}
	
	public Signal getWatchedSignal() {
		return watchedSignal;
	}

}
