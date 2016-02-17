package com.rxc.events;

import rx.Subscriber;

public class NextEvent<T> implements Event<T> {
    private final T item;

    public NextEvent(T item) {
        this.item = item;
    }

    @Override
    public void perform(Subscriber<? super T> subscriber) {
        subscriber.onNext(item);
    }

    @Override
    public void describe() {

    }
}
