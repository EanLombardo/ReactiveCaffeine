package com.rxc;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class ObservableBuilderTest {

    @Test
    public void completion() throws Exception {
        final Observable<String> testObj = new ObservableBuilder<String>()
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void noCompletion() throws Exception {
        final Observable<String> testObj =new ObservableBuilder<String>()
                .thenNeverCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        Thread.sleep(1000);

        subscriber.assertNoTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void emission() throws Exception {
        final Observable<String> testObj = new ObservableBuilder<String>()
                .thenEmits("Hello")
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertReceivedOnNext(Collections.singletonList("Hello"));
    }

    @Test
    public void error() throws Exception {
        final Observable<String> testObj = new ObservableBuilder<String>()
                .thenErrors(new IOException())
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertError(IOException.class);
    }

    @Test
    public void action() throws Exception {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        final Observable<String> testObj = new ObservableBuilder<String>()
                .then(new Action1<Subscriber<? super String>>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        atomicBoolean.set(true);
                    }
                })
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertTrue(atomicBoolean.get());
    }

    @Test
    public void test_wait() throws Exception {
        final Observable<String> testObj = new ObservableBuilder<String>()
                .thenWaits(1000)
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        final long startMills = System.currentTimeMillis();
        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        final long endMills = System.currentTimeMillis();

        assertTrue(endMills - startMills > 1000);
    }
}