package com.codependent.rx.sample3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;

public class SensorObservable extends Observable<Integer>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorObservable.class);
	
	protected SensorObservable(Integer start, Integer range) {
		super( (s) ->{
			for (int i = start; i < start + range; i++) {
				LOGGER.info("OBSERVABLE GENERATED [{}]", i);
				s.onNext(i);
			}
			s.onCompleted();
		});
	}
}
