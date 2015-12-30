package com.codependent.rx.samplescada.sensor.impl;

import rx.Observable;

import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;

public class FakeJamMachineBeltPositionSensor extends PositionSensor{

	private double objectPosition;
	protected Double sensorPosition;
	
	public FakeJamMachineBeltPositionSensor(Observable<Signal> observable, Double sensorPosition) {
		super(observable, sensorPosition);
		/*
		 * (s) -> {
			if(objectPosition == sensorPosition){
				s.onNext(new Signal(Type.SENSOR_REACHED));
				s.onCompleted();
			}
		}
		 */
	}
	
	public Double getSensorPosition() {
		return sensorPosition;
	}
	
	public void setObjectPosition(double objectPosition){
		this.objectPosition = objectPosition;
	}

}
