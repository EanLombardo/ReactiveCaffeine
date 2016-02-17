package com.rxc;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class ObservableEventChainBuilderTest {

    @Test
    public void testEventChain_completion() throws Exception {
        final Observable<String> testObj = new ObservableEventChainBuilder<String>()
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void testEventChain_noCompletion() throws Exception {
        final Observable<String> testObj = new ObservableEventChainBuilder<String>()
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
    public void testEventChain_emission() throws Exception {
        final Observable<String> testObj = new ObservableEventChainBuilder<String>()
                .thenEmits("Hello")
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertReceivedOnNext(Collections.singletonList("Hello"));
    }

    @Test
    public void testEventChain_error() throws Exception {
        final Observable<String> testObj = new ObservableEventChainBuilder<String>()
                .thenErrors(new IOException())
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertError(IOException.class);
    }

    @Test
    public void testEventChain_action() throws Exception {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        final Observable<String> testObj = new ObservableEventChainBuilder<String>()
                .then(new Action1<Subscriber<? super String>>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
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
    public void testEventChain_wait() throws Exception {
        final Observable<String> testObj = new ObservableEventChainBuilder<String>()
                .thenWaits(1000)
                .thenCompletes()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

        long startMills = System.currentTimeMillis();
        testObj.subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        long endMills = System.currentTimeMillis();

        assertTrue(endMills - startMills > 1000);
    }
}