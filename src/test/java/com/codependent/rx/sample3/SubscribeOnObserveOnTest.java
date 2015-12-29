package com.codependent.rx.sample3;


import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import rx.Observable;
import rx.schedulers.Schedulers;

@Test
public class SubscribeOnObserveOnTest {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SubscribeOnObserveOnTest.class);
	
	public void testObserveOn() throws InterruptedException{
		LOGGER.info("Starting");
		CountDownLatch latch = new CountDownLatch(1);
		SensorReaderObserver sensor = new SensorReaderObserver(()->{
			latch.countDown();});
		SensorObservable sensorObservable = new SensorObservable(1, 100);
		Observable<Integer> observable = sensorObservable
			.doOnTerminate(() -> {
				LOGGER.info("doOnTerminate!!!");
			}).observeOn(Schedulers.computation());
		
		observable.subscribe(sensor);
		
		LOGGER.info("Waiting");
		latch.await();
		LOGGER.info("Waking up");
		Assert.assertEquals(sensor.getValues().size(), 100);
		LOGGER.info("Finished");
	}
	
	public void testSubscribeOn() throws InterruptedException{
		LOGGER.info("Starting");
		CountDownLatch latch = new CountDownLatch(1);
		Observable<Integer> observable = Observable.range(1, 100)
			.doOnCompleted(() -> {
				LOGGER.info("doOnCompleted!!!");
				latch.countDown();
			})
			.subscribeOn(Schedulers.computation());
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		observable.subscribe(sensor1);
		
		LOGGER.info("Waiting");
		latch.await();
		LOGGER.info("Waking up");
		Assert.assertEquals(sensor1.getValues().size(), 100);
		LOGGER.info("Finished");
	}
	
	public void testSubscribeOnObserveOn() throws InterruptedException{
		LOGGER.info("Starting");
		CountDownLatch latch = new CountDownLatch(1);
		SensorObservable sensorObservable = new SensorObservable(1, 100);
		Observable<Integer> observable = sensorObservable
			.doOnCompleted(() -> {
				LOGGER.info("doOnCompleted!!!");
			})
			.subscribeOn(Schedulers.computation())
			.observeOn(Schedulers.computation());
		
		SensorReaderObserver sensor = new SensorReaderObserver(()->{
			latch.countDown();
		});
		observable.subscribe(sensor);
		
		LOGGER.info("Waiting");
		latch.await();
		LOGGER.info("Waking up");
		
		Assert.assertEquals(sensor.getValues().size(), 100);
	}
	
}
