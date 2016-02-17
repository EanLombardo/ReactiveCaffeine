package com.rxc.events;

import rx.Subscriber;

public interface Event<T> {

    void perform(Subscriber<? super T> subscriber);

    void describe();
}
