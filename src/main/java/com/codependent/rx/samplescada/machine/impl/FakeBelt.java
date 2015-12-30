package com.codependent.rx.samplescada.machine.impl;

import com.codependent.rx.samplescada.machine.Belt;

public class FakeBelt extends Belt{

	protected double speed;
	protected double objectPosition;
	
	public FakeBelt(double length, double speed) {
		super(length, speed);
		this.objectPosition = 0;
	}

	@Override
	public void doOnBeltStart() {
		while(getState() == State.STARTED){
			
			
		}
	}

	@Override
	public void doOnBeltStop() {
	}

	@Override
	public void addJar() {
		if(isEmptyBelt()){
			emptyBelt = false;
		}
		
	}

}
