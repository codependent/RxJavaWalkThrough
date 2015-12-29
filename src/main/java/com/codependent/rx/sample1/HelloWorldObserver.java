package com.codependent.rx.sample1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observer;

public class HelloWorldObserver implements Observer<String>{

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldObserver.class);
	
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
		LOGGER.info("OBSERVER [{}] FINISHED", this);
	}

	public void onError(Throwable e) {
		failed = true;
		LOGGER.error("OBSERVER [{}]: ", this, e);
	}

	public void onNext(String t) {
		message = t;
		LOGGER.info("OBSERVER [{}] GOT: [{}]", this, message);
	}

}
