package com.codependent.rx.samplescada.sensor;

public class Signal {

	public enum Type {JAR_IN_JARMACHINE, JAR_IN_BELT_END};
	
	private Type type;
	
	public Signal(Type type){
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
