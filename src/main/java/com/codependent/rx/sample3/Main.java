package com.codependent.rx.sample3;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import rx.Observable;
import rx.Subscriber;


public class Main {

	public static void main(String[] args) {
		Observable<Duration> stopWatch = Observable.create(
			(Subscriber<? super Duration> s) -> {
				Thread t = new Thread( () -> {
					Clock clock = Clock.systemDefaultZone();
					Instant instant = clock.instant();
					while(!s.isUnsubscribed() && true){
						Duration duration = Duration.between(instant, clock.instant());
						s.onNext( duration );
					}
				});
				t.start();
			}
		);
		
		StopWatchObserver observer = new StopWatchObserver("John", 10);
		stopWatch.subscribe(observer);
		
		observer = new StopWatchObserver("Michael", 5);
		stopWatch.subscribe(observer);
	}

}
