package com.codependent.rx.sample2;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

import org.testng.Assert;
import org.testng.annotations.Test;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@Test
public class StopWatchTest {

	public void testStopWatch() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(2);
		
		Observable<Duration> stopWatch = Observable.create(
			(Subscriber<? super Duration> s) -> {
				Thread t = new Thread( () -> {
					Clock clock = Clock.systemDefaultZone();
					Instant instant = clock.instant();
					while(!s.isUnsubscribed() && true){
						Duration duration = Duration.between(instant, clock.instant());
						s.onNext( duration );
					}
					s.onCompleted();
				});
				t.start();
			}
		).doOnTerminate( () -> {
			System.out.println("STOPWATCH FINISHED");
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			latch.countDown();
		});
		
		
		StopWatchObserver observer = new StopWatchObserver("John", 10);
		stopWatch.subscribe(observer);
		
		StopWatchObserver observer2 = new StopWatchObserver("Michael", 5);
		stopWatch.subscribe(observer2);
		
		latch.await();
		
		Assert.assertTrue(observer.getFinishTime().isAfter(observer2.getFinishTime()));
	}
	
	public void testStopWatch2() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(2);
		
		Observable<Duration> stopWatch = Observable.create(
			(Subscriber<? super Duration> s) -> {
				Clock clock = Clock.systemDefaultZone();
				Instant instant = clock.instant();
				while(!s.isUnsubscribed() && true){
					Duration duration = Duration.between(instant, clock.instant());
					s.onNext( duration );
				}
				s.onCompleted();
			}
		).doOnTerminate( () -> {
			System.out.println("STOPWATCH FINISHED");
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			latch.countDown();
		}).subscribeOn(Schedulers.io());
		
		
		StopWatchObserver observer = new StopWatchObserver("John", 10);
		stopWatch.subscribe(observer);
		
		StopWatchObserver observer2 = new StopWatchObserver("Michael", 5);
		stopWatch.subscribe(observer2);
		
		latch.await();
		
		Assert.assertTrue(observer.getFinishTime().isAfter(observer2.getFinishTime()));
	}
	
}
