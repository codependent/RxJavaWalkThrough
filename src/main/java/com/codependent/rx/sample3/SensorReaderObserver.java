package com.codependent.rx.sample3;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class SensorReaderObserver extends Subscriber<Integer>{

	private List<Integer> values = new ArrayList<Integer>();

	public List<Integer> getValues() {
		return values;
	}
	
	public void onCompleted() {
		System.out.printf("OBSERVER %s FINISHED\n", this);
	}
	
	public void onError(Throwable e) {
		System.err.printf("OBSERVER %s ERROR: %s\n", this, e);
	}
	
	public void onNext(Integer value) {
		values.add(value);
		System.out.printf("OBSERVER %s GOT: %s\n", this, value);
	}
}
