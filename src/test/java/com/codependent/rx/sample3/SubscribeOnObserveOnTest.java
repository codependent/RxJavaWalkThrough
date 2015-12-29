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
		SensorReaderObserver sensor = new SensorReaderObserver();
		SensorObservable sensorObservable = new SensorObservable(1, 1000);
		Observable<Integer> observable = sensorObservable
			.doOnTerminate(() -> {
				LOGGER.info("doOnTerminate!!!");
				while(sensor.getValues().size()!=1000){
					try{Thread.sleep(100);
					}catch(InterruptedException e){}
				}
				Assert.assertEquals(sensor.getValues().size(), 1000);
			}).observeOn(Schedulers.computation());
		
		observable.subscribe(sensor);
		LOGGER.info("Finished");
	}
	
	public void testSubscribeOn() throws InterruptedException{
		LOGGER.info("Starting");
		CountDownLatch latch = new CountDownLatch(1);
		Observable<Integer> observable = Observable.range(1, 1000)
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
		Assert.assertEquals(sensor1.getValues().size(), 1000);
		LOGGER.info("Finished");
	}
	
	public void testSubscribeOnObserveOn() throws InterruptedException{
		LOGGER.info("Starting");
		CountDownLatch latch = new CountDownLatch(1);
		SensorObservable sensorObservable = new SensorObservable(1, 1000);
		Observable<Integer> observable = sensorObservable
			.doOnCompleted(() -> {
				LOGGER.info("doOnCompleted!!!");
				latch.countDown();
			})
			.subscribeOn(Schedulers.computation())
			.observeOn(Schedulers.computation());
		
		SensorReaderObserver sensor1 = new SensorReaderObserver();
		observable.subscribe(sensor1);
		
		LOGGER.info("Waiting");
		latch.await();
		LOGGER.info("Waking up");
		
		Assert.assertEquals(sensor1.getValues().size(), 1000);
	}
	
}
