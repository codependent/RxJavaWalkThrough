package com.codependent.rx.samplescada.sensor.impl;

public class FakeSignal {

	public enum Type {
		BELT_STARTED,
		BELT_STOPPED
	}
	
	private Type type;
	
	public FakeSignal(Type type){
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
