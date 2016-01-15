package com.codependent.rx.samplescada.machine.impl;

import org.springframework.stereotype.Component;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.JamMachine;
import com.codependent.rx.samplescada.machine.Signal;
import com.codependent.rx.samplescada.machine.Signal.Type;

@Component
public class FakeJamMachine extends JamMachine{

	private int level;
	
	public FakeJamMachine(){
		super("jamMachine");
		Observable<Signal> obs = Observable.<Signal>create( (s) -> {
			while(state != State.STOPPED){
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(filling){
					while(state == State.OPERATING && level < 100){
						try {
							Thread.sleep(200);
						} catch (Exception e) {
							e.printStackTrace();
						}
						level += 10;
						logger.info("Filling jar - level {}%", level);
						s.onNext(new Signal(Type.JAR_IN_JARMACHINE_FILLING_INFO, level+""));
					}
					if(level == 100){
						s.onNext(new Signal(Type.JARMACHINE_JAR_FILLED));
						filling = false;
						level = 0;
					}
				}
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.io());
		observable = obs.publish();
	}
	
	@Override
	protected void doOnStart() {
		observable.connect();
	}
	
	@Override
	protected void doOnStop() {
		super.doOnStop();
	}
	
	@Override
	protected void doOnStartOperating() {
		filling = true;
	}
	
	@Override
	protected void doOnStopOperating() {
		super.doOnStopOperating();
	}
	
	

}
