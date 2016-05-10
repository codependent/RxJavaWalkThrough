package com.codependent.rx.sample4.rx;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import rx.Single;
import rx.SingleSubscriber;

@Component
public class SingleTxFactory {
	
	public final <T> Single<T> create(Single.OnSubscribe<T> f) {
        return new SingleTx<T>(this, f);
    }

	@Transactional
    public <T> void call(Single.OnSubscribe<T> onSubscribe, SingleSubscriber<? super T> subscriber) {
        onSubscribe.call(subscriber);
    }

    private static class SingleTx<T> extends Single<T> {

        public SingleTx(SingleTxFactory singleTxFactory, OnSubscribe<T> f) {
            super(new OnSubscribeDecorator<>(singleTxFactory, f));
        }
    }

    private static class OnSubscribeDecorator<T> implements Single.OnSubscribe<T> {

        private final SingleTxFactory singleTxFactory;
        private final Single.OnSubscribe<T> onSubscribe;

        OnSubscribeDecorator(final SingleTxFactory singleTxFactory, final Single.OnSubscribe<T> s) {
            this.onSubscribe = s;
            this.singleTxFactory = singleTxFactory;
        }

		@Override
		public void call(SingleSubscriber<? super T> subscriber) {
			singleTxFactory.call(onSubscribe, subscriber);
		}
    }
}
