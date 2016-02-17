package com.rxc.events;

import rx.Subscriber;

public class CompletionEvent<T> implements Event<T>{
    @Override
    public void perform(Subscriber<? super T> subscriber) {
        subscriber.onCompleted();
    }

    @Override
    public void describe() {

    }
}
