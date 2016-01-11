package com.codependent.rx.samplescada.machine.impl;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.JamMachine;
import com.codependent.rx.samplescada.sensor.Signal;
import com.codependent.rx.samplescada.sensor.Signal.Type;

public class FakeJamMachine extends JamMachine{

	public FakeJamMachine(){
		Observable<Signal> obs = Observable.<Signal>create( (s) -> {
			logger.info("Filling jar");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			s.onNext(new Signal(Type.JARMACHINE_JAR_FILLED));
			s.onCompleted();
		}).subscribeOn(Schedulers.io());
		observable = obs.publish();
	}
	
	@Override
	public void doOnStart() {
		observable.connect();
	}
	
	@Override
	public void doOnStop() {
		// TODO Auto-generated method stub
		super.doOnStop();
	}
}
