package com.codependent.rx.samplescada.machine.impl;

import java.util.List;

import rx.Observable;

import com.codependent.rx.samplescada.machine.Belt;
import com.codependent.rx.samplescada.sensor.PositionSensor;
import com.codependent.rx.samplescada.sensor.impl.FakeJamMachineBeltPositionSensor;
import com.codependent.rx.samplescada.sensor.impl.FakeSignal;

public class FakeBelt extends Belt{

	protected double speed;
	
	public FakeBelt(double length, double speed, List<PositionSensor> positionSensors) {
		super(length, speed, positionSensors);
	}

	@Override
	public void doOnBeltStart() {
		Observable<FakeSignal> obs = Observable.<FakeSignal>create( (s) -> {
			s.onNext(new FakeSignal(FakeSignal.Type.BELT_STARTED));
		});
		for (PositionSensor sensor : positionSensors) {
			if(sensor instanceof FakeJamMachineBeltPositionSensor){
				obs.subscribe((FakeJamMachineBeltPositionSensor)sensor);
				break;
			}
		}
	}

	@Override
	public void doOnBeltStop() {
		Observable<FakeSignal> obs = Observable.<FakeSignal>create( (s) -> {
			s.onNext(new FakeSignal(FakeSignal.Type.BELT_STOPPED));
		});
		for (PositionSensor sensor : positionSensors) {
			if(sensor instanceof FakeJamMachineBeltPositionSensor){
				obs.subscribe((FakeJamMachineBeltPositionSensor)sensor);
				break;
			}
		}
	}

	@Override
	public void addJar() {
		logger.info("Adding jar");
		if(isEmptyBelt()){
			emptyBelt = false;
		}
		
	}

}
