package com.rxc;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({Subscriber.class})
@RunWith(PowerMockRunner.class)
public class DisposeBagTest{

    @Mock
    private Subscriber mockSubscriber;

    @Test
    public void norm(){
        final List<Subscription> subscriptionList = new ArrayList<Subscription>(10);

        final DisposeBag disposeBag = new DisposeBag();

        for (int i = 0; i < 10; i++) {
            final Subscription empty = Subscriptions.empty();
            subscriptionList.add(empty);
            disposeBag.add(empty);
        }

        disposeBag.disposeAll();

        for(final Subscription subscription : subscriptionList){
            assertTrue(subscription.isUnsubscribed());
        }
    }

    @Test
    public void addAll(){
        final List<Subscription> subscriptionList = new ArrayList<Subscription>(10);

        final DisposeBag disposeBag = new DisposeBag();

        for (int i = 0; i < 10; i++) {
            subscriptionList.add(Subscriptions.empty());
        }
        disposeBag.addAll(subscriptionList);

        disposeBag.disposeAll();

        for(final Subscription subscription : subscriptionList){
            assertTrue(subscription.isUnsubscribed());
        }
    }

    @Test
    public void mixed(){
        final List<Subscription> subscriptionList = new ArrayList<Subscription>(10);

        final DisposeBag disposeBag = new DisposeBag();

        for (int i = 0; i < 10; i++) {
            final Subscription empty = i % 2 == 0?Subscriptions.empty(): Subscriptions.unsubscribed();
            subscriptionList.add(empty);
            disposeBag.add(empty);
        }

        disposeBag.disposeAll();

        for(final Subscription subscription : subscriptionList){
            assertTrue(subscription.isUnsubscribed());
        }
    }

    @Test
    public void dereferenced(){
        final DisposeBag disposeBag = new DisposeBag();

        for (int i = 0; i < 10; i++) {
            disposeBag.add(Subscriptions.empty());
        }

        System.gc();

        disposeBag.disposeAll();
        //Assert no exceptions
    }

    @Test
    public void unsubscribeNotCalled_ifAlreadyUnsubscribed(){
        final DisposeBag disposeBag = new DisposeBag();
        when(mockSubscriber.isUnsubscribed()).thenReturn(true);

        disposeBag.add(mockSubscriber);

        disposeBag.disposeAll();

        verify(mockSubscriber,never()).unsubscribe();
    }

    @Test
    public void unsubcribesAll_evenWithExceptions(){
        final DisposeBag disposeBag = new DisposeBag();
        when(mockSubscriber.isUnsubscribed()).thenReturn(false);
        doThrow(new RuntimeException()).when(mockSubscriber).unsubscribe();

        disposeBag.add(mockSubscriber);
        disposeBag.add(mockSubscriber);
        disposeBag.add(mockSubscriber);

        try {
            disposeBag.disposeAll();
        } catch (final Exception ignore){}

        verify(mockSubscriber,times(3)).unsubscribe();
    }

}