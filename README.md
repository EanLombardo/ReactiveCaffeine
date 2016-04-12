# ReactiveCaffeine
[![Build Status](https://travis-ci.org/EanLombardo/ReactiveCaffeine.svg?branch=master)](https://travis-ci.org/EanLombardo/ReactiveCaffeine)

ReactiveCaffeine is a library that contains all sorts of things to make RXJava easier to use.

## Features
* DisposeBag
  * DisposeBage makes handling unsubscription of groups of Subscriptions easy. It allows you to add all of the Subscriptions and dispose of all of them easily. DisposeBag is re-usable and only holds on to subscriptions via weak references so you never have to worry about large subscribers living on and taking up valuable memory.
* ObservableBuilder
  * Sometimes you just need an Observable that emmits 6 items, sleeps 30 seconds and errors. Normally you would have to use Observable.create and write all the code out for this. Or you can just use ObservableBuilder, which allows you to build Observables that do exactly what you want. It's perfect for buildind up Observables that need to do something odd for testing purposes, or exploratory coding.
