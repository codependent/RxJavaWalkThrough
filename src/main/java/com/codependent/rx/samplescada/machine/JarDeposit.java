package com.codependent.rx.samplescada.machine;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import com.codependent.rx.samplescada.machine.Signal.Type;



public abstract class JarDeposit extends Machine{

	protected ConnectableObservable<Signal> observable;
	protected final JarDepositContainer container;
	private volatile boolean drop;
	
	public JarDeposit(String id, int capacity){
		super(id);
		this.container = new JarDepositContainer(capacity);
		observable = Observable.<Signal>create( (s) -> {
			while(state != Machine.State.STOPPED){
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(state == State.OPERATING && drop){
					if(container.getCapacity() > 0 ){
						doDropJar();
						Signal signal = new Signal(Type.JARDEPOSIT_DROPPED_JAR);
						s.onNext(signal);
						signal = new Signal(Type.JARDEPOSIT_CAPACITY, container.getCapacity()+"");
						s.onNext(signal);
						container.decreaseCapacity();
					}else{
						Signal signal = new Signal(Type.JARDEPOSIT_EMPTY);
						s.onNext(signal);
					}
					stopOperating();
				}
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.io())
		.publish();
	}
	
	@Override
	protected void doOnStart() {
		observable.connect();
	}

	@Override
	protected void doOnStop() {
	}
	
	@Override
	protected void doOnStartOperating() {
		doDropJar();
		drop = true;
	}
	
	@Override
	protected void doOnStopOperating() {
		drop = false;
	}
	
	public void setCapacity(Integer number){
		container.setCapacity(number);
	}
	
	public ConnectableObservable<Signal> getObservable() {
		return observable;
	}
	
	protected abstract void doDropJar();
	
	private class JarDepositContainer{
		private int capacity;
		
		JarDepositContainer(Integer capacity){
			this.capacity = capacity;
		}
		
		public int getCapacity() {
			return capacity;
		}
		
		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}
		
		public void decreaseCapacity(){
			this.capacity--;
		}
	}
}
