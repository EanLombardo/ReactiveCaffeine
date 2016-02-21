package com.rxc.events;

import rx.Subscriber;

public class SleepEvent<T> implements Event<T> {
    private final long sleepTime;

    public SleepEvent(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void perform(Subscriber<? super T> subscriber) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
