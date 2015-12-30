package com.codependent.rx.samplescada.sensor;

import rx.Observable;


public abstract class PositionSensor extends Sensor{

	protected Double sensorPosition;
	
	public PositionSensor(Observable<Signal> observable, Double sensorPosition){
		super(observable);
		this.sensorPosition = sensorPosition;
	}
	
	public Double getSensorPosition() {
		return sensorPosition;
	}
}
