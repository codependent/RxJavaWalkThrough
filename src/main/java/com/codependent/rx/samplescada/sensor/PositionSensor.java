package com.codependent.rx.samplescada.sensor;



public abstract class PositionSensor extends Sensor{

	protected Double sensorPosition;
	
	public PositionSensor(Double sensorPosition){
		super();
		this.sensorPosition = sensorPosition;
	}
	
	public Double getSensorPosition() {
		return sensorPosition;
	}
}
