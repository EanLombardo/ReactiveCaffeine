package com.rxc.events;

import rx.Subscriber;

public class SleepEvent<T> implements Event<T> {
    private final long sleepTime;

    public SleepEvent(final long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void perform(final Subscriber<? super T> subscriber) {
        try {
            Thread.sleep(sleepTime);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
