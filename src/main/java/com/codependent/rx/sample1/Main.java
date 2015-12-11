package com.codependent.rx.sample1;

import rx.Subscriber;

public class Main {

	public static void main(String[] args) {
		
		HelloWorldObservable observable = new HelloWorldObservable( (Subscriber<? super String> t) -> 
			{ System.out.printf("ON SUBSCRIBE %s\n", t);
			  t.onNext("Hello World!");
			  t.onCompleted();
			}
		);
		
		HelloWorldObserver observer = new HelloWorldObserver();
		observable.subscribe(observer);
		
		observer = new HelloWorldObserver();
		observable.subscribe(observer);
	}

}
