package com.codependent.rx.samplescada.machine.robot.impl;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.Signal;
import com.codependent.rx.samplescada.machine.robot.TranslatorRobot;

public class FakeTranslatorRobot extends TranslatorRobot{

	private volatile boolean gotTranslationOrder;
	private volatile boolean needsToGoBack;
	private volatile boolean translatingJar;
	private volatile Double currentTime = 0.0;
	private Double totalTime;
	
	public FakeTranslatorRobot(String id, Double totalTime) {
		super(id);
		this.totalTime = totalTime;
		observable = Observable.<Signal>create( (s) -> {
			while(state != State.STOPPED){
				try {
					Thread.sleep(16);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(gotTranslationOrder){
					if(currentTime == 0.0 && !needsToGoBack){
						s.onNext(new Signal(Signal.Type.TRANSLATIONROBOT_PICKINGUP_JAR));
						translatingJar = true;
						gotTranslationOrder = false;
					}
				}
				while(translatingJar || needsToGoBack){
					while(currentTime <= this.totalTime){
						try {
							Thread.sleep(16);
						} catch (Exception e) {
							e.printStackTrace();
						}
						currentTime += 0.016;
						if(translatingJar){
							logger.info("Translating jar - {} of {}", currentTime, this.totalTime);
							s.onNext(new Signal(Signal.Type.TRANSLATIONROBOT_TRANSLATING, currentTime+""));
						}else{
							logger.info("Returning - {} of {}", currentTime, this.totalTime);
							s.onNext(new Signal(Signal.Type.TRANSLATIONROBOT_RETURNING, currentTime+""));
						}
					}
					if(translatingJar){
						logger.info("Jar translated");
						translatingJar = false;
						needsToGoBack = true;
						currentTime = 0.0;
						s.onNext(new Signal(Signal.Type.TRANSLATIONROBOT_TRANSLATED));
					}else{
						logger.info("Robot returned");
						needsToGoBack = false;
						currentTime = 0.0;
						s.onNext(new Signal(Signal.Type.TRANSLATIONROBOT_RETURNED, currentTime+""));
					}
				}
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.io())
		.publish();
	}

	@Override
	protected void doOnStartOperating() {
		gotTranslationOrder = true;
	}

	@Override
	protected void doOnStopOperating() {
		
	}
	
	public void setTotalTime(Double totalTime) {
		this.totalTime = totalTime;
	}

}
