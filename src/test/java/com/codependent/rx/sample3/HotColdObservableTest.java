package com.codependent.rx.sample3;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import rx.Observable;
import rx.Observer;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

@Test
public class HotColdObservableTest {

	@Test
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
	
	@Test
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
	
	@Test
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
	
	@Test
	public void testHotObservable3() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		ConnectableObservable<Integer> observable = Observable.<Integer>create( (s) -> {
			while(true){
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("["+Thread.currentThread().getName()+ "] observable");
				s.onNext(1);
			}
		})
		
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.io())
		.publish();
		
		Observer<Integer> observer = new Observer<Integer>() {
			@Override
			public void onNext(Integer i) {
				System.out.println("["+Thread.currentThread().getName()+ "] got "+i);
			}
			@Override
			public void onCompleted() {
				System.out.println("completed");
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}
		};
		
		observable.subscribe(observer);
		System.out.println("["+Thread.currentThread().getName()+ "]");
		observable.connect();
		
		latch.await();
	}
	
	@Test
	public void testHotObservable4() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		PublishSubject<Integer> subject = PublishSubject.<Integer>create();
		
		ConnectableObservable<Integer> series = Observable.<Object, Long, Integer>zip(
				Observable.range(1, 10) , 
				Observable.interval(1, TimeUnit.SECONDS),
					(val, timer) -> (Integer)val)
			    .doOnNext(item -> {
			    	System.out.println("["+Thread.currentThread().getName()+ "] Emitting item: "+item);
			    })
			    .doOnCompleted( () -> latch.countDown()).publish();
		
		series.subscribe(subject);	
		Thread.sleep(2000);
		System.out.println("WAKE UP 1");
		series.connect();
		Thread.sleep(2000);
		System.out.println("WAKE UP 2");
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		subject.subscribe(sensor1);
		Thread.sleep(4000);
		System.out.println("WAKE UP 3");
		SensorReaderObserver sensor2 = new SensorReaderObserver();
		subject.subscribe(sensor2);
		
		latch.await();
		
		Assert.assertEquals(sensor1.getValues().size(), 8);
		Assert.assertEquals(sensor2.getValues().size(), 4);
	}
	
}
