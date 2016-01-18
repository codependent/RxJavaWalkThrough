package com.codependent.rx.samplescada.machine;

import java.util.List;

import rx.Observer;

import com.codependent.rx.samplescada.machine.sensor.PositionSensor;
import com.codependent.rx.samplescada.machine.Signal;


public abstract class Belt extends Machine implements Observer<Signal>{
	
	protected double lenght;
	protected double speed;
	protected boolean empty = true;
	protected List<PositionSensor> positionSensors;
	
	public Belt(String id, double length, double speed, List<PositionSensor> positionSensors){
		super(id);
		this.lenght = length;
		this.speed = speed;
		this.positionSensors = positionSensors;
	}
	
	public abstract void addEmptyJar();
	public abstract void removeFullJar();

	public boolean isEmpty() {
		return empty;
	}
	
	public double getLenght() {
		return lenght;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	@Override
	protected void doOnStart(){
		doOnBeltStart();
	}
	
	@Override
	protected void doOnStop(){
		doOnBeltStop();
	}
	
	@Override
	protected void doOnStartOperating(){
		doOnBeltStartOperating();
	}
	
	@Override
	protected void doOnStopOperating(){
		doOnBeltStopOperating();
	}
	
	protected abstract void doOnBeltStart();
	protected abstract void doOnBeltStop();
	protected abstract void doOnBeltStartOperating();
	protected abstract void doOnBeltStopOperating();
	
	@Override
	public void onNext(Signal s) {
		if(s.getType() == Signal.Type.JAR_IN_JARMACHINE ||
		   s.getType() == Signal.Type.JAR_IN_BELT_END){
			stop();
		}
	}
	
	@Override
	public void onCompleted() {
		logger.debug("Completed");
	}
	
	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}
	
}
