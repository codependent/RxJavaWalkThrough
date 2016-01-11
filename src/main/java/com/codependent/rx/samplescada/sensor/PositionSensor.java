package com.codependent.rx.samplescada.sensor;

import com.codependent.rx.samplescada.machine.Belt;



public abstract class PositionSensor extends Sensor{

	protected Belt belt;
	protected Double sensorPosition;
	
	public PositionSensor(Belt belt, Double sensorPosition){
		super();
		this.sensorPosition = sensorPosition;
		this.belt = belt;
	}
	
	public Double getSensorPosition() {
		return sensorPosition;
	}
}
