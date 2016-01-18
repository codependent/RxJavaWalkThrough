package com.codependent.rx.samplescada.machine;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import rx.Observer;

import com.codependent.rx.samplescada.machine.Signal.Type;
import com.codependent.rx.samplescada.machine.impl.FakeBelt;
import com.codependent.rx.samplescada.machine.impl.FakeJamMachine;
import com.codependent.rx.samplescada.machine.impl.FakeJarDeposit;
import com.codependent.rx.samplescada.machine.sensor.PositionSensor;
import com.codependent.rx.samplescada.machine.sensor.impl.FakeBeltPositionSensor;

@Component
public class Scada extends Machine implements Observer<Signal>{

	private Belt belt;
	
	private JamMachine jamMachine;
	
	private JarDeposit jarDeposit;
	
	private PositionSensor jamMachineBeltSensor;
	
	private PositionSensor beltEndSensor;
	
	@Autowired(required=false)
	private SimpMessagingTemplate messagingTemplate;
	
	private CountDownLatch latch = new CountDownLatch(1);

	@Autowired
	public Scada(Belt belt, JarDeposit jarDeposit, JamMachine jamMachine, @Qualifier("jamMachineBeltSensor") PositionSensor jamMachineBeltSensor, @Qualifier("beltEndSensor") PositionSensor beltEndSensor){
		super("scada");
		this.belt = belt;
		this.jarDeposit = jarDeposit;
		this.jamMachine = jamMachine;
		this.jamMachineBeltSensor = jamMachineBeltSensor;
		this.beltEndSensor = beltEndSensor;
	}
	
	@Override
	protected void doOnStart() {
		jamMachineBeltSensor.start();
		jamMachineBeltSensor.getObservable().subscribe(this);
		jamMachineBeltSensor.getLifecycleObservable().subscribe(this);
		
		beltEndSensor.start();
		beltEndSensor.getObservable().subscribe(this);
		beltEndSensor.getLifecycleObservable().subscribe(this);
		
		jamMachine.start();
		jamMachine.getObservable().subscribe(this);
		jamMachine.getLifecycleObservable().subscribe(this);
		
		jarDeposit.start();
		jarDeposit.getObservable().subscribe(this);
		jarDeposit.getLifecycleObservable().subscribe(this);
		
		belt.start();
		belt.getLifecycleObservable().subscribe(this);
	}

	@Override
	protected void doOnStop() {}
	
	@Override
	protected void doOnStartOperating() {
		if(jamMachine.isFilling()){
			jamMachine.startOperating();
		}else{ 
			if(belt.isEmpty()){
				jarDeposit.startOperating();
			}else{
				belt.startOperating();
			}
		}
	}
	
	@Override
	protected void doOnStopOperating() {
		jamMachine.stopOperating();
		belt.stopOperating();
	}
	
	public void setNumberOfJarsInDeposit(Integer number){
		jarDeposit.setCapacity(number);
	}
	
	public void setBeltSpeed(Double speed){
		belt.setSpeed(speed);;
	}
	
	public static void main(String[] args) throws InterruptedException {
		PositionSensor jamMachineBeltSensor = new FakeBeltPositionSensor("jamMachineBeltSensor", new Double[]{0.0, 4.9}, 0.0, 1.0, new Signal(Type.JAR_IN_JARMACHINE));
		PositionSensor beltEndSensor = new FakeBeltPositionSensor("beltEndSensor", new Double[]{4.9, 10.0}, 5.0, 1.0, new Signal(Type.JAR_IN_BELT_END));
		Belt belt = new FakeBelt("belt", 10.0, 0.10, Arrays.asList(new PositionSensor[]{jamMachineBeltSensor,beltEndSensor}));
		JarDeposit jarDeposit = new FakeJarDeposit("jarDeposit", 5);
		JamMachine jamMachine = new FakeJamMachine();
		
		Scada scada = new Scada(belt, jarDeposit, jamMachine, jamMachineBeltSensor, beltEndSensor);
		scada.start();
		
		scada.startOperating();
		
		Thread.sleep(4000);
		scada.logger.info("HALT!!!!!!");
		scada.stopOperating();
		Thread.sleep(4000);
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
		logger.info("{}",s);
		if(messagingTemplate!=null){
			messagingTemplate.convertAndSend("/topic/ui", s);
		}
		if(s.getType() == Type.JARDEPOSIT_DROPPED_JAR){
			belt.addEmptyJar();
			belt.startOperating();
		}else if(s.getType() == Type.JARDEPOSIT_EMPTY){
			
		}else if(s.getType() == Type.JARDEPOSIT_DROPPED_JAR){
			
		}else if(s.getType() == Type.JAR_IN_JARMACHINE_FILLING_INFO){
			
		}else if(s.getType() == Type.JAR_IN_JARMACHINE){
			belt.stopOperating();
			jamMachine.startOperating();
		}else if(s.getType() == Type.JARMACHINE_JAR_FILLED){
			jamMachine.stopOperating();
			belt.startOperating();
		}else if(s.getType() == Type.JAR_IN_BELT_POSITION){
			
		}else if(s.getType() == Type.JAR_IN_BELT_END){
			belt.stopOperating();
			belt.removeFullJar();
			jarDeposit.doDropJar();
			jarDeposit.startOperating();
		}
	}
	
	@Override
	public void onCompleted() {
		logger.info("****************** ON COMPLETED *******************");
	}

	@Override
	public void onError(Throwable e) {
		logger.error("****************** ON ERROR ******************* {}",  e);
	}

}
