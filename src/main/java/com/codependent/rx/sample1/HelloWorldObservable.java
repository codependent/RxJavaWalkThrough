package com.codependent.rx.sample1;

import rx.Observable;

public class HelloWorldObservable extends Observable<String>{
	
	public HelloWorldObservable(rx.Observable.OnSubscribe<String> f) {
		super(f);
	}
	
}
