package com.codependent.rx.sample1;

import rx.Observer;

public class HelloWorldObserver implements Observer<String>{

	private String message;
	private boolean failed;
	private boolean completed;
	
	public String getMessage() {
		return message;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public boolean isFailed() {
		return failed;
	}
	
	public void onCompleted() {
		completed = true;
		System.out.printf("OBSERVER %s FINISHED\n", this);
	}

	public void onError(Throwable e) {
		failed = true;
		System.err.printf("OBSERVER %s ERROR: %s\n", this, e);
	}

	public void onNext(String t) {
		message = t;
		System.out.printf("OBSERVER %s GOT: %s\n", this, message);
	}

}
