package com.rxc;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * A builder to build up an Observable that performs a set of events on it's Subscribers
 * This is the unsafe builder, meant for testing odd circumstances and working with Observables
 * that don't follow the strict contract. All events given to the builder will be performed on the Subscriber
 *
 * @param <T> The class for the Observable
 */
public class ObservableBuilder<T> {

    private final UnsafeObservableBuilder<T> builder = new UnsafeObservableBuilder<>();

    /**
     * Adds a new event to the observable that emits the given item
     * @param item The item to emit
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> emit(final T item){
        builder.emit(item);
        return this;
    }

    /**
     * Builds an Observable that performs all previous events and lastly terminates with the given error
     * @param error The error
     * @return The final Observable
     */
    public Observable<T> error(final Throwable error){
        builder.error(error);
        return builder.build();
    }

    /**
     * Builds an Observable that performs all previous events and lastly terminates by telling it's subscribers that it completed
     * @return The final Observable
     */
    public Observable<T> complete(){
        builder.complete();
        return builder.build();
    }

    /**
     * Adds a new event to the observable that will delay all further event's by the specified amount of time
     * @param mills How long to delay in milliseconds
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> sleep(final long mills){
        builder.sleep(mills);
        return this;
    }

    /**
     * Adds a new event to the observable that performs the specified action on the subscription
     * @param action The action to be performed
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> perform(final Action1<Subscriber<? super T>> action){
        builder.perform(action);
        return this;
    }

    /**
     * Adds a new event to the observable that runs the specified runnable
     * @param runnable The runnable to run
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> perform(final Runnable runnable){
        builder.perform(runnable);
        return this;
    }

    /**
     * Returns an Observable the performs all event's given to the builder when subscribed to
     * @return The Observable
     */
    public Observable<T> build(){
        return builder.build();
    }
}
