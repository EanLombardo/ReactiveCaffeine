package com.rxc.events;

import rx.Subscriber;
import rx.functions.Action1;

public class ActionEvent<T> implements Event<T> {
    private final Action1<Subscriber<? super T>> action;

    public ActionEvent(Action1<Subscriber<? super T>> action) {
        this.action = action;
    }

    @Override
    public void perform(Subscriber<? super T> subscriber) {
        action.call(subscriber);
    }

    @Override
    public void describe() {

    }
}
