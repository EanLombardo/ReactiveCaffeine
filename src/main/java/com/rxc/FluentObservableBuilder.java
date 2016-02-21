package com.rxc;

import com.rxc.events.*;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import java.util.LinkedList;
import java.util.List;

public class FluentObservableBuilder<T> {
    private final List<Event<T>> events = new LinkedList<Event<T>>();

    FluentObservableBuilder(){

    }

    FluentObservableBuilder<T> thatStarts(){
        events.add(new StartEvent<T>());
        return this;
    }

    public FluentObservableBuilder<T> thenEmits(final T item){
        events.add(new NextEvent<T>(item));
        return this;
    }

    public FluentObservableBuilder<T> thenErrors(final Throwable error){
        events.add(new ErrorEvent<T>(error));
        return this;
    }

    public FluentObservableBuilder<T> thenWaits(final long mills){
        events.add(new SleepEvent<T>(mills));
        return this;
    }

    public FluentObservableBuilder<T> then(final Action1<Subscriber<? super T>> action){
        events.add(new ActionEvent<T>(action));
        return this;
    }

    public Observable<T> thenCompletes(){
        events.add(new CompletionEvent<T>());
        return build();
    }

    public Observable<T> thenNeverCompletes(){
        return build();
    }

    private Observable<T> build(){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                for(Event<T> event : events){
                    event.perform(subscriber);
                }
            }
        });
    }
}
