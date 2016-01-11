package com.codependent.rx.samplescada.machine.impl;

import com.codependent.rx.samplescada.machine.Belt;

public class FakeBelt extends Belt{

	protected double speed;
	
	public FakeBelt(double length, double speed) {
		super(length, speed);
	}

	@Override
	public void doOnBeltStart() {
		/*while(getState() == State.STARTED){
			
			
		}*/
	}

	@Override
	public void doOnBeltStop() {
	}

	@Override
	public void addJar() {
		logger.info("Adding jar");
		if(isEmptyBelt()){
			emptyBelt = false;
		}
		
	}

}
