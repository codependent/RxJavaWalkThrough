package com.codependent.rx.samplescada.machine.impl;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.JamMachine;
import com.codependent.rx.samplescada.sensor.Signal;
import com.codependent.rx.samplescada.sensor.Signal.Type;

public class FakeJamMachine extends JamMachine{

	private boolean filling;
	
	public FakeJamMachine(){
		Observable<Signal> obs = Observable.<Signal>create( (s) -> {
			while(true){
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(filling){
					logger.info("Filling jar");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					s.onNext(new Signal(Type.JARMACHINE_JAR_FILLED));
					filling = false;
				}
			}
		}).subscribeOn(Schedulers.io());
		observable = obs.publish();
	}
	
	@Override
	public void doOnStart() {
		observable.connect();
		filling = true;
	}
	
	@Override
	public void doOnStop() {
		// TODO Auto-generated method stub
		super.doOnStop();
	}
}
