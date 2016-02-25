package com.rxc.events;

import rx.Subscriber;

public class NextEvent<T> implements Event<T> {
    private final T item;

    public NextEvent(final T item) {
        this.item = item;
    }

    public T getValue(){
        return item;
    }

    @Override
    public void perform(final Subscriber<? super T> subscriber) {
        subscriber.onNext(item);
    }

    @Override
    public String toString() {
        return "onNext( " + item.toString() + " )";
    }
}
