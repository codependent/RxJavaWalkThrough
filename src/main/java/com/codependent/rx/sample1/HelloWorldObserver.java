package com.codependent.rx.sample1;

import rx.Observer;

public class HelloWorldObserver implements Observer<String>{

	public void onCompleted() {
		System.out.printf("OBSERVER %s FINISHED\n", this);
	}

	public void onError(Throwable e) {
		System.err.printf("OBSERVER %s ERROR: %s\n", this, e);
	}

	public void onNext(String t) {
		System.out.printf("OBSERVER %s GOT: %s\n", this, t);
	}

}
