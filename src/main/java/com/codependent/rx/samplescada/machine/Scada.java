package com.codependent.rx.samplescada.machine;

import rx.Observer;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.impl.FakeBelt;
import com.codependent.rx.samplescada.machine.impl.FakeJamMachine;
import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;
import com.codependent.rx.samplescada.sensor.impl.FakeJamMachineBeltPositionSensor;

public class Scada extends Machine implements Observer<Signal>{

	private Belt belt;
	private JamMachine jamMachine;
	private PositionSensor jamMachineBeltSensor;
	ConnectableObservable<Signal> jamMachineBeltSensorObservable;
	
	public Scada(Belt belt, JamMachine jamMachine, PositionSensor jamMachineBeltSensor){
		this.belt = belt;
		this.jamMachine = jamMachine;
		this.jamMachineBeltSensor = jamMachineBeltSensor;
	}
	
	@Override
	public void doOnStart() {
		jamMachineBeltSensorObservable = jamMachineBeltSensor.subscribeOn(Schedulers.io()).publish();
	}

	@Override
	public void doOnStop() {
		
	}
	
	public void startProduction(){
		if(belt.isEmptyBelt()){
			belt.addJar();
		}
		belt.start();
		jamMachineBeltSensorObservable.subscribe(belt);
	}
	
	public void stopProduction(){
		belt.stop();
		jamMachine.stop();
	}

	public static void main(String[] args) {
		
		PositionSensor jamMachineBeltSensor = new FakeJamMachineBeltPositionSensor(5.0);
		Belt belt = new FakeBelt(10.0, 1.0);
		JamMachine jamMachine = new FakeJamMachine();
		
		Scada scada = new Scada(belt, jamMachine, jamMachineBeltSensor);
		scada.start();
		
		scada.startProduction();
	}
}
