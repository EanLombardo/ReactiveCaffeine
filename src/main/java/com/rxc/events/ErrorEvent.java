package com.rxc.events;

import rx.Subscriber;

public class ErrorEvent<T> implements Event<T> {
    private final Throwable error;

    public ErrorEvent(Throwable error) {
        this.error = error;
    }

    @Override
    public void perform(Subscriber<? super T> subscriber) {
        subscriber.onError(error);
    }
}
