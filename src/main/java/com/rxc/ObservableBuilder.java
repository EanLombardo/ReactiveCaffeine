package com.rxc;

import com.rxc.events.*;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import java.util.LinkedList;
import java.util.List;

/**
 * A builder to build FluentObservables
 * @param <T> The class for the Observable
 */
public class ObservableBuilder<T> {
    private final List<Event<T>> events = new LinkedList<Event<T>>();

    /**
     * Adds a new event to the observable that simply emits the item given
     * @param item The item to emit
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> thenEmits(final T item){
        events.add(new NextEvent<T>(item));
        return this;
    }

    /**
     * Adds a new event to the observable that errors using the given error
     * @param error The error
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> thenErrors(final Throwable error){
        events.add(new ErrorEvent<T>(error));
        return this;
    }

    /**
     * Adds a new event to the observable that will delay all further event's by the specified amount of time
     * @param mills How long to delay in milliseconds
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> thenWaits(final long mills){
        events.add(new SleepEvent<T>(mills));
        return this;
    }

    /**
     * Adds a new event to the observable that performs the specified action on the subscription
     * @param action The action to be performed
     * @return The builder to build the rest of the observable with
     */
    public ObservableBuilder<T> then(final Action1<Subscriber<? super T>> action){
        events.add(new ActionEvent<T>(action));
        return this;
    }

    /**
     * Builds the final FluentObservable, the last action of the Observable will be to notify it's subscribers that it completed
     * @return The Observable that will emit the defined events when subscribed to
     */
    public Observable<T> thenCompletes(){
        events.add(new CompletionEvent<T>());
        return build();
    }

    /**
     * Builds the final FluentObservable, the observable will stop performing events, but it will never inform it's subscribers of it's completion
     * @return The Observable that will emit the defined events when subscribed to
     */
    public Observable<T> thenNeverCompletes(){
        return build();
    }

    private Observable<T> build(){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                for(final Event<T> event : events){
                    event.perform(subscriber);
                }
            }
        });
    }
}
