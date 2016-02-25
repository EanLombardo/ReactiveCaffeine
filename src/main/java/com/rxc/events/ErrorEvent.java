package com.rxc.events;

import rx.Subscriber;

public class ErrorEvent<T> implements Event<T> {
    private final Throwable error;

    public ErrorEvent(final Throwable error) {
        this.error = error;
    }

    public Throwable getError(){
        return error;
    }

    @Override
    public void perform(final Subscriber<? super T> subscriber) {
        subscriber.onError(error);
    }

    @Override
    public String toString() {
        return "onError( { class: " + error.getClass().getSimpleName() + " , message: \"" + error.getMessage() + "\" } )";
    }
}
