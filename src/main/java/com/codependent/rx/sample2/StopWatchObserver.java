package com.codependent.rx.sample2;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import rx.Subscriber;

public class StopWatchObserver extends Subscriber<Duration>{

	private String name;
	private int maxTime;
	private Instant finishTime;
	
	public StopWatchObserver(String name, int maxTime){
		this.name = name;
		this.maxTime = maxTime;
	}
	
	public Instant getFinishTime() {
		return finishTime;
	}
	
	public void onCompleted() {
		finishTime = Clock.systemDefaultZone().instant();
		System.out.printf("[%s] OBSERVER %s FINISHED\n", Thread.currentThread().getName(), name);
	}

	public void onError(Throwable e) {
		System.err.printf("[%s] OBSERVER %s ERROR: %s\n", Thread.currentThread().getName(), name, e);
	}

	public void onNext(Duration t) {
		System.out.printf("[%s] OBSERVER %s GOT: %s\n", Thread.currentThread().getName(), name, t);
		if(t.getSeconds() >= maxTime){
			unsubscribe();
		}
	}

}
