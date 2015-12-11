package com.codependent.rx.sample2;

import rx.Observable;

public class Main {

	public static void main(String[] args) {
		
		Observable<String> observable = Observable.just("Hello World!");
		
		HelloWorldObserver observer = new HelloWorldObserver();
		observable.subscribe(observer);
		
		observer = new HelloWorldObserver();
		observable.subscribe(observer);
	}

}
