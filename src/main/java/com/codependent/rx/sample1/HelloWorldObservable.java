package com.codependent.rx.sample1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;

public class HelloWorldObservable extends Observable<String>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldObservable.class);
	
	public HelloWorldObservable() {
		this(false);
	}
	
	public HelloWorldObservable(boolean fail) {
		super( t -> { 
			if(fail){
				t.onError(new RuntimeException("Fail"));
			}else{
				LOGGER.info("ON SUBSCRIBE [{}]", t);
				t.onNext("Hello World!");
				t.onCompleted();
			}
		});
	}
	
}
