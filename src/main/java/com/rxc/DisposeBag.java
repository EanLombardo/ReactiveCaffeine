package com.rxc;

import rx.Subscription;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class DisposeBag {
    private final List<WeakReference<Subscription>> subscriptions = new LinkedList<WeakReference<Subscription>>();

    private final ReferenceQueue<Subscription> referenceQueue = new ReferenceQueue<Subscription>();

    public void add(final Subscription subscription){
        synchronized (referenceQueue) {
            subscriptions.add(new WeakReference<Subscription>(subscription, referenceQueue));
        }
    }

    public void addAll(final Iterable<Subscription> subscriptions){
        synchronized (referenceQueue) {
            for (final Subscription subscription : subscriptions) {
                this.subscriptions.add(new WeakReference<Subscription>(subscription, referenceQueue));
            }
        }
    }

    public void disposeAll(){
        synchronized (referenceQueue) {
            for (final WeakReference<Subscription> ref : subscriptions) {
                final Subscription subscription = ref.get();

                if (subscription != null && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }

                ref.clear();
            }

            cleanupReferences();
            subscriptions.clear();
        }
    }

    private void cleanupReferences(){
        WeakReference deadRef = (WeakReference)referenceQueue.poll();
        while (deadRef != null){
            subscriptions.remove(deadRef);
            deadRef = (WeakReference)referenceQueue.poll();
        }
    }
}
