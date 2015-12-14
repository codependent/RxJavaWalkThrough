package com.codependent.rx.sample1;

import org.junit.Assert;
import org.testng.annotations.Test;

import rx.Observable;

@Test
public class HelloWorldTest {

	public void testHelloWorld(){
		HelloWorldObservable observable = new HelloWorldObservable();
		HelloWorldObserver observer = new HelloWorldObserver();
		observable.subscribe(observer);
		
		Assert.assertEquals("Hello World!", observer.getMessage());
		Assert.assertTrue(observer.isCompleted());
		
		observable = new HelloWorldObservable(true);
		observer = new HelloWorldObserver();
		observable.subscribe(observer);
	
		Assert.assertTrue(observer.isFailed());
	}
	
	public void testHelloWorld2(){
		Observable<String> observable = Observable.just("Hello World!");
		
		HelloWorldObserver observer = new HelloWorldObserver();
		observable.subscribe(observer);
		
		Assert.assertEquals("Hello World!", observer.getMessage());
		Assert.assertTrue(observer.isCompleted());
	}
	
}
