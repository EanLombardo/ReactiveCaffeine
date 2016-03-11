package com.rxc;

import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple system for tearing down subscriptions in a memory efficient and re-usable manner
 *
 * DisposeBag allows you to add as many subscriptions as you like, perform you can call disposeAll() to unsubscribe
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
    private final List<WeakReference<Subscription>> subscriptions = new LinkedList<>();

    private final ReferenceQueue<Subscription> referenceQueue = new ReferenceQueue<>();

    /**
     * Adds a subscription to the DisposeBag
     * @param subscription The subscription to add
     */
    public void add(final Subscription subscription){
        synchronized (referenceQueue) {
            subscriptions.add(new WeakReference<>(subscription, referenceQueue));
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
                this.subscriptions.add(new WeakReference<>(subscription, referenceQueue));
            }
        }

        cleanupReferences();
    }

    /**
     * Unsubscribes all added subscribers that are still subscribed and haven't been garbage collected
     * The DisposeBag is still usable after this method is called
     *
     * An attempt to unsubscribe is made on every subscription in the DisposeBag, even if a subscription throws
     * If any subscriptions do throw they are removed from the dispose bag and a Composite exception is thrown
     * that contains all throws at the end of the disposal process
     */
    public void disposeAll(){
        final List<Throwable> errors = new LinkedList<>();
        synchronized (referenceQueue) {
            for (final WeakReference<Subscription> ref : subscriptions) {
                try {
                    final Subscription subscription = ref.get();

                    if (subscription != null && !subscription.isUnsubscribed()) {
                        subscription.unsubscribe();
                    }
                    ref.clear();
                } catch (final Throwable t){
                    errors.add(t);
                }
            }

            cleanupReferences();
            subscriptions.clear();

            Exceptions.throwIfAny(errors);
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
