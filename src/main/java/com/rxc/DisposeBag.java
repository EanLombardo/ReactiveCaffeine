package com.rxc;

import rx.Subscription;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple system for tearing down subscriptions in a memory efficient and re-usable manner
 *
 * DisposeBag allows you to add as many subscriptions as you like, then you can call disposeAll() to unsubscribe
 * all added subscriptions. This is necessary for many application structures, especially UI applications
 * in order to destroy all active subscriptions before part of a system dies.
 *
 * The when the DisposeBag disposes it's subscriptions it is still fully usable,
 * more subscriptions can be added and disposed at any time
 *
 * Dispose bag itself only ever weakly references subscriptions. So if a subscription added to the dispose bag
 * unsubscribes and becomes unreferenced elsewhere, it won't hang around in memory.
 *
 * DisposeBag also cleans up any internal dead references on every interaction, preventing it from bloating in size
 */
public class DisposeBag {
    private final List<WeakReference<Subscription>> subscriptions = new LinkedList<WeakReference<Subscription>>();

    private final ReferenceQueue<Subscription> referenceQueue = new ReferenceQueue<Subscription>();

    /**
     * Adds a subscription to the DisposeBag
     * @param subscription The subscription to add
     */
    public void add(final Subscription subscription){
        synchronized (referenceQueue) {
            subscriptions.add(new WeakReference<Subscription>(subscription, referenceQueue));
        }

        cleanupReferences();
    }

    /**
     * Adds a collection of subscriptions to the DisposeBag
     * @param subscriptions The subscriptions to add
     */
    public void addAll(final Iterable<Subscription> subscriptions){
        synchronized (referenceQueue) {
            for (final Subscription subscription : subscriptions) {
                this.subscriptions.add(new WeakReference<Subscription>(subscription, referenceQueue));
            }
        }

        cleanupReferences();
    }

    /**
     * Unsubscribes all added subscribers that are still subscribed and haven't been garbage collected
     * The DisposeBag is still usable after this method is called
     */
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