package com.codependent.rx.samplescada.sensor;


public class Signal {

	public enum Type {
		JAR_IN_JARMACHINE, 
		JAR_IN_JARMACHINE_FILLING_INFO,
		JAR_IN_BELT_END,
		JARMACHINE_JAR_FILLED};
	
	private Type type;
	private String info;
	
	public Signal(Type type){
		this.type = type;
	}
	
	public Signal(Type type, String info){
		this.type = type;
		this.info = info;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getInfo() {
		return info;
	}

	@Override
	public String toString() {
		return "Signal [type=" + type + ", info=" + info + "]";
	}
	
}
