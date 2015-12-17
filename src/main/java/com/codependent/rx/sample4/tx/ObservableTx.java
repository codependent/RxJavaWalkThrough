package com.codependent.rx.sample4.tx;

import javax.transaction.Transactional;

import rx.Observable;
import rx.Subscriber;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;

public class ObservableTx<T> extends Observable<T> {
	
	private static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();
	
    protected ObservableTx(OnSubscribe<T> f) {
        super(new OnSubscribeDecorator<>(f));
    }
    
    public final static <T> ObservableTx<T> createTx(OnSubscribe<T> f) {
        return new ObservableTx<T>(hook.onCreate(f));
    }

    private static class OnSubscribeDecorator<T> implements OnSubscribe<T> {

        private final OnSubscribe<T> onSubscribe;

        OnSubscribeDecorator(final OnSubscribe<T> s) {
            this.onSubscribe = s;
        }

        @Transactional
        @Override
        public void call(Subscriber<? super T> subscriber) {
            this.onSubscribe.call(subscriber);
        }
    }
}