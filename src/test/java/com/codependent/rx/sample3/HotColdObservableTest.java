package com.codependent.rx.sample3;


import java.util.concurrent.CountDownLatch;

import org.testng.Assert;
import org.testng.annotations.Test;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

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
		SensorReaderObserver sensor2 = new SensorReaderObserver(100);
		
		observable.subscribe(sensor1);
		observable.subscribe(sensor2);
		observable.connect();
		
		latch.await();
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		Assert.assertEquals(sensor2.getValues().size(), 100);
		
	}
	
	public void testHotObservable3() throws InterruptedException{
		
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
	
	public void testGoodMemorySubject() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(1);
		
		ConnectableObservable<Integer> observable = Observable.range(1, 1000)
				.doOnCompleted(() -> {
					latch.countDown();
				})
				.subscribeOn(Schedulers.io())
				.publish();
		
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		ReplaySubject<Integer> goodMemorySubject = ReplaySubject.<Integer>create();
		SensorReaderObserver sensor2 = new SensorReaderObserver();
		
		observable.subscribe(sensor1);
		observable.subscribe(goodMemorySubject);
		observable.connect();
		Thread.sleep(40);
		goodMemorySubject.subscribe(sensor2);
		
		latch.await();
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		Assert.assertEquals(sensor2.getValues().size(), 1000);
		Assert.assertEquals(goodMemorySubject.getValues().length, 1000);
	}
	
	public void testRealTimeSubject() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(1);
		
		ConnectableObservable<Integer> observable = Observable.range(1, 1000)
				.doOnCompleted(() -> {
					latch.countDown();
				})
				.subscribeOn(Schedulers.io())
				.publish();
		
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		PublishSubject<Integer> realTimeSubject = PublishSubject.<Integer>create();
		SensorReaderObserver sensor2 = new SensorReaderObserver();
		
		observable.subscribe(sensor1);
		observable.subscribe(realTimeSubject);
		observable.connect();
		Thread.sleep(40);
		realTimeSubject.subscribe(sensor2);
		
		latch.await();
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		Assert.assertNotEquals(sensor2.getValues().size(), 1000);
	}
	
	public void testLastValueSubject() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(1);
		
		ConnectableObservable<Integer> observable = Observable.range(1, 1000)
				.doOnCompleted(() -> {
					latch.countDown();
				})
				.subscribeOn(Schedulers.io())
				.publish();
		
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		AsyncSubject<Integer> lastValueSubject = AsyncSubject.<Integer>create();
		SensorReaderObserver sensor2 = new SensorReaderObserver();
		
		observable.subscribe(sensor1);
		observable.subscribe(lastValueSubject);
		observable.connect();
		Thread.sleep(40);
		lastValueSubject.subscribe(sensor2);
		
		latch.await();
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		Assert.assertEquals(sensor2.getValues().size(), 1);
	}
	
}
