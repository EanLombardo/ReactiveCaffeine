package com.rxc.events;

import rx.Subscriber;

public class StartEvent<T> implements Event<T> {
    @Override
    public void perform(Subscriber<? super T> subscriber) {
        subscriber.onStart();
    }
}
