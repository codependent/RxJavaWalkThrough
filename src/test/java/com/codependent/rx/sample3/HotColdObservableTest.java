package com.codependent.rx.sample3;


import java.util.concurrent.CountDownLatch;

import org.testng.Assert;
import org.testng.annotations.Test;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

@Test
public class HotColdObservableTest {

	public void testColdObservable() throws InterruptedException{
		Observable<Integer> observable = Observable.range(1, 1000);
		
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		SensorReaderObserver sensor2 = new SensorReaderObserver();
		
		observable.subscribe(sensor1);
		Thread.sleep(1000);
		observable.subscribe(sensor2);
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		Assert.assertEquals(sensor2.getValues().size(), 1000);
		
	}
	
	public void testHotObservable() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		ConnectableObservable<Integer> observable = Observable.range(1, 1000)
			.doOnCompleted(() -> {
				latch.countDown();
			})
			.subscribeOn(Schedulers.io())
			.publish();
		
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		SensorReaderObserver sensor2 = new SensorReaderObserver();
		
		observable.subscribe(sensor1);
		observable.connect();
		Thread.sleep(40);
		observable.subscribe(sensor2);
		
		latch.await();
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		Assert.assertNotEquals(sensor2.getValues().size(), 1000);
		
	}
	
	public void testHotObservable2() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		ConnectableObservable<Integer> observable = Observable.range(1, 1000)
				.doOnCompleted(() -> {
					latch.countDown();
				})
				.subscribeOn(Schedulers.io())
				.publish();
		
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		SensorReaderObserver sensor2 = new SensorReaderObserver();
		
		observable.subscribe(sensor1);
		observable.subscribe(sensor2);
		
		observable.connect();
		
		latch.await();
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		Assert.assertEquals(sensor2.getValues().size(), 1000);
		
	}
	
}
