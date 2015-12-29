package com.codependent.rx.sample3;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Subscriber;
import rx.functions.Action0;

public class SensorReaderObserver extends Subscriber<Integer>{

	private static final Logger LOGGER = LoggerFactory.getLogger(SensorReaderObserver.class);
	
	private Integer maxInterested;
	private List<Integer> values = new ArrayList<Integer>();
	private Action0 onCompleted;

	public SensorReaderObserver(){}
	
	public SensorReaderObserver(Integer maxInterested){
		this.maxInterested = maxInterested;
	}
	
	public SensorReaderObserver(final Action0 onCompleted){
		this.onCompleted = onCompleted;
	}
	
	public SensorReaderObserver(Integer maxInterested, final Action0 onCompleted){
		this.maxInterested = maxInterested;
		this.onCompleted = onCompleted;
	}
	
	public List<Integer> getValues() {
		return values;
	}
	
	public void onCompleted() {
		LOGGER.info("OBSERVER [{}] FINISHED", this);
		if(onCompleted!=null){
			onCompleted.call();
		}
	}
	
	public void onError(Throwable e) {
		LOGGER.error("OBSERVER [{}]", this, e);
	}
	
	public void onNext(Integer value) {
		values.add(value);
		LOGGER.info("OBSERVER [{}] GOT: [{}]", this, value);
		if(maxInterested!=null && maxInterested <= values.size()){
			LOGGER.info("OBSERVER [{}] REACHED MAX VALUES [{}] - UNSUBSCRIBING", this, maxInterested);
			unsubscribe();
		}
	}
}
