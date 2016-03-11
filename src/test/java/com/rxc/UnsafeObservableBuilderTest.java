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

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class UnsafeObservableBuilderTest {

    @Test
    public void completion() throws Exception {
        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .complete()
                .build()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<>();
        testObj.unsafeSubscribe(subscriber);

        subscriber.awaitTerminalEvent();

        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void empty() throws Exception {
        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .build()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<>();

        testObj.unsafeSubscribe(subscriber);
        Thread.sleep(1000);

        subscriber.assertNoTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void emission() throws Exception {
        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .emit("Hello")
                .complete()
                .build()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<>();

        testObj.unsafeSubscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertReceivedOnNext(Collections.singletonList("Hello"));
    }

    @Test
    public void error() throws Exception {
        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .error(new IOException())
                .complete()
                .build()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<>();

        testObj.unsafeSubscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertError(IOException.class);
    }

    @Test
    public void action() throws Exception {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .perform(new Action1<Subscriber<? super String>>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        atomicBoolean.set(true);
                    }
                })
                .complete()
                .build()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<>();

        testObj.unsafeSubscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertTrue(atomicBoolean.get());
    }

    @Test
    public void runnable() throws Exception {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .perform(new Runnable() {
                    @Override
                    public void run() {
                        atomicBoolean.set(true);
                    }
                })
                .complete()
                .build()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<>();

        testObj.unsafeSubscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertTrue(atomicBoolean.get());
    }

    @Test
    public void test_wait() throws Exception {
        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .sleep(1000)
                .complete()
                .build()
                .observeOn(Schedulers.newThread());

        final TestSubscriber<String> subscriber = new TestSubscriber<>();

        final long startMills = System.currentTimeMillis();
        testObj.unsafeSubscribe(subscriber);
        subscriber.awaitTerminalEvent();
        final long endMills = System.currentTimeMillis();

        assertTrue(endMills - startMills >= 1000);
    }

    @Test
    public void test_unsafeness(){
        final Observable<String> testObj = new UnsafeObservableBuilder<String>()
                .complete()
                .sleep(1000)
                .complete()
                .error(new Exception())
                .emit("hi")
                .build();

        final TestSubscriber<String> subscriber = new TestSubscriber<>();
        testObj.unsafeSubscribe(subscriber);

        subscriber.assertValue("hi");
        subscriber.assertError(Exception.class);

        try {
            subscriber.assertCompleted();
            fail("Did not complete multiple times");
        } catch (final AssertionError error){
            assertThat(error.getLocalizedMessage(),containsString("multiple times"));
        }

    }
}