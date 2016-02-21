package com.rxc;

/**
 * A simple system to create an observable that performs a series of events
 */
public class FluentObservable {

    /**
     * Starts building a FluentObservable that will notify it's subscribers once it starts emitting events
     * @param clazz The class for the type of Observable
     * @param <T> The class for the type of Observable
     * @return A FluentObservableBuilder to construct the rest of the Observable with
     */
    public static <T> FluentObservableBuilder<T> thatStarts(Class<T> clazz) {
        return new FluentObservableBuilder<T>().thatStarts();
    }

    /**
     * Starts building a FluentObservable that will not notify it's subscribers once it starts emitting events
     * @param clazz The class for the type of Observable
     * @param <T> The class for the type of Observable
     * @return A FluentObservableBuilder to construct the rest of the Observable with
     */
    public static <T> FluentObservableBuilder<T> thatNeverStarts(Class<T> clazz){
        return new FluentObservableBuilder<T>();
    }
}
