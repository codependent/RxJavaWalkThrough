package com.codependent.rx.samplescada.machine;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import rx.Observer;

import com.codependent.rx.samplescada.machine.impl.FakeBelt;
import com.codependent.rx.samplescada.machine.impl.FakeJamMachine;
import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.Signal;
import com.codependent.rx.samplescada.sensor.Signal.Type;
import com.codependent.rx.samplescada.sensor.impl.FakeBeltPositionSensor;

public class Scada extends Machine implements Observer<Signal>{

	private Belt belt;
	private JamMachine jamMachine;
	private PositionSensor jamMachineBeltSensor;
	private PositionSensor beltEndSensor;
	
	private CountDownLatch latch = new CountDownLatch(1);

	
	public Scada(Belt belt, JamMachine jamMachine, PositionSensor jamMachineBeltSensor, PositionSensor beltEndSensor){
		this.belt = belt;
		this.jamMachine = jamMachine;
		this.jamMachineBeltSensor = jamMachineBeltSensor;
		this.beltEndSensor = beltEndSensor;
	}
	
	@Override
	public void doOnStart() {
		jamMachineBeltSensor.start();
		jamMachineBeltSensor.getObservable().subscribe(this);
		
		beltEndSensor.start();
		beltEndSensor.getObservable().subscribe(this);
		
		jamMachine.start();
		jamMachine.getObservable().subscribe(this);
		
		belt.start();
	}

	@Override
	public void doOnStop() {}
	
	@Override
	public void doOnStartOperating() {
		if(jamMachine.isFilling()){
			jamMachine.startOperating();
		}else{ 
			if(belt.isEmptyBelt()){
				belt.addEmptyJar();
			}
			belt.startOperating();
		}
	}
	
	@Override
	public void doOnStopOperating() {
		jamMachine.stopOperating();
		belt.stopOperating();
	}
	
	public static void main(String[] args) throws InterruptedException {
		PositionSensor jamMachineBeltSensor = new FakeBeltPositionSensor(new Double[]{0.0, 4.9}, 0.0, 1.0, new Signal(Type.JAR_IN_JARMACHINE));
		PositionSensor beltEndSensor = new FakeBeltPositionSensor(new Double[]{5.0, 10.0}, 5.0, 1.0, new Signal(Type.JAR_IN_BELT_END));
		Belt belt = new FakeBelt(10.0, 1.0, Arrays.asList(new PositionSensor[]{jamMachineBeltSensor,beltEndSensor}));
		
		JamMachine jamMachine = new FakeJamMachine();
		
		Scada scada = new Scada(belt, jamMachine, jamMachineBeltSensor, beltEndSensor);
		scada.start();
		
		scada.startOperating();
		
		Thread.sleep(4000);
		scada.logger.info("HALT!!!!!!");
		scada.stopOperating();
		Thread.sleep(10000);
		scada.startOperating();
		Thread.sleep(4000);
		scada.logger.info("HALT!!!!!!");
		scada.stopOperating();
		Thread.sleep(4000);
		scada.startOperating();
		
		scada.latch.await();
	}

	@Override
	public void onNext(Signal s) {
		if(s.getType() == Type.JAR_IN_JARMACHINE){
			belt.stopOperating();
			jamMachine.startOperating();
		}else if(s.getType() == Type.JARMACHINE_JAR_FILLED){
			jamMachine.stopOperating();
			belt.startOperating();
		}else if(s.getType() == Type.JAR_IN_BELT_END){
			belt.stopOperating();
			belt.removeFullJar();
			belt.addEmptyJar();
			belt.startOperating();
		}
	}
	
	@Override
	public void onCompleted() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
	}

}
