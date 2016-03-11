package com.rxc;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import java.util.LinkedList;
import java.util.List;

/**
 * A builder to build up an Observable that performs a set of events on it's Subscribers
 * This is the unsafe builder, meant for testing odd circumstances and working with Observables
 * that don't follow the strict contract. All events given to the builder will be performed on the Subscriber,
 * even if the Subscriber would normally terminate. This also allows the Observable to emit
 * more than one error, or complete more than once. Though in order for the
 * subscriber to be called in such a manner you must subscribe with Observable.unsafeSubscribe()
 *
 * You should not be using this unless you purposefully are violating the RX Observable contract, or are testing odd situations
 * Instead use ObservableBuilder, which enforces a proper RX Observable contract
 *
 * @param <T> The class for the Observable
 */
public final class UnsafeObservableBuilder<T> {
    private final List<Action1<Subscriber<? super T>>> events = new LinkedList<>();

    /**
     * Adds a new event to the observable that emits the given item
     * @param item The item to emit
     * @return The builder to build the rest of the observable with
     */
    public UnsafeObservableBuilder<T> emit(final T item){
        events.add(new Action1<Subscriber<? super T>>() {
            @Override
            public void call(final Subscriber<? super T> tSubscriber) {
                tSubscriber.onNext(item);
            }
        });
        return this;
    }

    /**
     * Adds a new event to the observable that errors using the given error
     * All further events will still be performed on the subscriber even though it's terminated
     * @param error The error
     * @return The builder to build the rest of the observable with
     */
    public UnsafeObservableBuilder<T> error(final Throwable error){
        events.add(new Action1<Subscriber<? super T>>() {
            @Override
            public void call(final Subscriber<? super T> tSubscriber) {
                tSubscriber.onError(error);
            }
        });
        return this;
    }

    /**
     * Builds the final FluentObservable, the last action of the Observable will be to notify it's subscribers that it completed
     * All further events will still be performed on the subscriber even though it's terminated
     * @return The Observable that will emit the defined events when subscribed to
     */
    public UnsafeObservableBuilder<T> complete(){
        events.add(new Action1<Subscriber<? super T>>() {
            @Override
            public void call(final Subscriber<? super T> tSubscriber) {
                tSubscriber.onCompleted();
            }
        });
        return this;
    }

    /**
     * Adds a new event to the observable that will delay all further event's by the specified amount of time
     * @param mills How long to delay in milliseconds
     * @return The builder to build the rest of the observable with
     */
    public UnsafeObservableBuilder<T> sleep(final long mills){
        events.add(new Action1<Subscriber<? super T>>() {
            @Override
            public void call(final Subscriber<? super T> tSubscriber) {
                try {
                    Thread.sleep(mills);
                } catch (final InterruptedException e) {
                   tSubscriber.onError(e);
                }
            }
        });
        return this;
    }

    /**
     * Adds a bew event that performs the given Notification through the Observable
     * @param notification The Notification to perform
     * @return The builder to build the rest of the observable with
     */
    public UnsafeObservableBuilder<T> notify(final Notification<T> notification){
        events.add(new Action1<Subscriber<? super T>>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                notification.accept(subscriber);
            }
        });
        return this;
    }

    /**
     * Adds a new event to the observable that performs the specified action on the subscription
     * @param action The action to be performed
     * @return The builder to build the rest of the observable with
     */
    public UnsafeObservableBuilder<T> perform(final Action1<Subscriber<? super T>> action){
        events.add(action);
        return this;
    }

    /**
     * Adds a new event to the observable that runs the specified runnable
     * @param runnable The runnable to run
     * @return The builder to build the rest of the observable with
     */
    public UnsafeObservableBuilder<T> perform(final Runnable runnable){
        events.add(new Action1<Subscriber<? super T>>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                runnable.run();
            }
        });
        return this;
    }

    /**
     * Returns an Observable the performs all event's given to the builder when subscribed to
     * @return The Observable
     */
    public Observable<T> build(){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                for(final Action1<Subscriber<? super T>> event : events){
                    event.call(subscriber);
                }
            }
        });
    }
}
