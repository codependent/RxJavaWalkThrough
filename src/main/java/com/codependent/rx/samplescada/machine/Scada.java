package com.codependent.rx.samplescada.machine;

import java.util.concurrent.CountDownLatch;

import rx.Observer;

import com.codependent.rx.samplescada.machine.impl.FakeBelt;
import com.codependent.rx.samplescada.machine.impl.FakeJamMachine;
import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;
import com.codependent.rx.samplescada.sensor.Signal.Type;
import com.codependent.rx.samplescada.sensor.impl.FakeJamMachineBeltPositionSensor;

public class Scada extends Machine implements Observer<Signal>{

	private Belt belt;
	private JamMachine jamMachine;
	private PositionSensor jamMachineBeltSensor;
	private CountDownLatch latch = new CountDownLatch(1); 
	
	public Scada(Belt belt, JamMachine jamMachine, PositionSensor jamMachineBeltSensor){
		this.belt = belt;
		this.jamMachine = jamMachine;
		this.jamMachineBeltSensor = jamMachineBeltSensor;
	}
	
	@Override
	public void doOnStart() {
		jamMachineBeltSensor.start();
		
	}

	@Override
	public void doOnStop() {
		
	}
	
	public void startProduction(){
		
		jamMachineBeltSensor.getObservable().subscribe(this);
		jamMachine.getObservable().subscribe(this);
		
		if(belt.isEmptyBelt()){
			belt.addJar();
		}
		
		belt.start();
		
		jamMachineBeltSensor.getObservable().connect();
	}
	
	public void stopProduction(){
		belt.stop();
		jamMachine.stop();
	}

	public static void main(String[] args) throws InterruptedException {
		Belt belt = new FakeBelt(10.0, 1.0);
		PositionSensor jamMachineBeltSensor = new FakeJamMachineBeltPositionSensor(belt, 5.0, 0.0);
		
		JamMachine jamMachine = new FakeJamMachine();
		
		Scada scada = new Scada(belt, jamMachine, jamMachineBeltSensor);
		scada.start();
		
		scada.startProduction();
		
		scada.latch.await();
	}

	@Override
	public void onCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNext(Signal s) {
		if(s.getType() == Type.JAR_IN_JARMACHINE){
			belt.stop();
			jamMachine.start();
		}else if(s.getType() == Type.JARMACHINE_JAR_FILLED){
			jamMachine.stop();
		}
		
	}
}
