package com.codependent.rx.samplescada.machine.sensor;




public abstract class PositionSensor extends Sensor{

	protected Double sensorPosition;
	
	public PositionSensor(String id, Double sensorPosition){
		super(id);
		this.sensorPosition = sensorPosition;
	}
	
	public Double getSensorPosition() {
		return sensorPosition;
	}
}
