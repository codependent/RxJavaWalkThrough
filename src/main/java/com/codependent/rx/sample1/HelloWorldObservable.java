package com.codependent.rx.sample1;

import rx.Observable;

public class HelloWorldObservable extends Observable<String>{
	
	public HelloWorldObservable() {
		this(false);
	}
	
	public HelloWorldObservable(boolean fail) {
		super( t -> { 
			if(fail){
				t.onError(new RuntimeException("Fail"));
			}else{
				System.out.printf("ON SUBSCRIBE %s\n", t);
				t.onNext("Hello World!");
				t.onCompleted();
			}
		});
	}
	
}
