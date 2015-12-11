package com.codependent.rx.sample3;

import java.time.Duration;

import rx.Subscriber;

public class StopWatchObserver extends Subscriber<Duration>{

	private String name;
	private int maxTime;
	
	public StopWatchObserver(String name, int maxTime){
		this.name = name;
		this.maxTime = maxTime;
	}
	
	public void onCompleted() {
		System.out.printf("OBSERVER %s FINISHED\n", name);
	}

	public void onError(Throwable e) {
		System.err.printf("OBSERVER %s ERROR: %s\n", name, e);
	}

	public void onNext(Duration t) {
		System.out.printf("OBSERVER %s GOT: %s\n", name, t);
		if(t.getSeconds() >= maxTime){
			unsubscribe();
		}
	}

}
