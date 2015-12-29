package com.codependent.rx.sample4.rx;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import rx.Observable;
import rx.Subscriber;

@Component
public class ObservableTxFactory {
	
	public final <T> Observable<T> create(Observable.OnSubscribe<T> f) {
        return new ObservableTx<T>(this, f);
    }

	@Transactional
    public <T> void call(Observable.OnSubscribe<T> onSubscribe, Subscriber<? super T> subscriber) {
        onSubscribe.call(subscriber);
    }

    private static class ObservableTx<T> extends Observable<T> {

        public ObservableTx(ObservableTxFactory observableTxFactory, OnSubscribe<T> f) {
            super(new OnSubscribeDecorator<>(observableTxFactory, f));
        }
    }

    private static class OnSubscribeDecorator<T> implements Observable.OnSubscribe<T> {

        private final ObservableTxFactory observableTxFactory;
        private final Observable.OnSubscribe<T> onSubscribe;

        OnSubscribeDecorator(final ObservableTxFactory observableTxFactory, final Observable.OnSubscribe<T> s) {
            this.onSubscribe = s;
            this.observableTxFactory = observableTxFactory;
        }

        @Override
        public void call(Subscriber<? super T> subscriber) {
            observableTxFactory.call(onSubscribe, subscriber);
        }
    }
}
