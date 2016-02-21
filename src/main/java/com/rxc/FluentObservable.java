package com.rxc;

public class FluentObservable {
    public static <T> FluentObservableBuilder<T> thatStarts(Class<T> clazz) {
        return new FluentObservableBuilder<T>().thatStarts();
    }

    public static <T> FluentObservableBuilder<T> thatNeverStarts(Class<T> clazz){
        return new FluentObservableBuilder<T>();
    }
}
