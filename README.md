# ReactiveCaffeine
[![Build Status](https://travis-ci.org/EanLombardo/ReactiveCaffeine.svg?branch=master)](https://travis-ci.org/EanLombardo/ReactiveCaffeine)[ ![Download](https://api.bintray.com/packages/eanlombardo/maven/ReactiveCaffeine/images/download.svg) ](https://bintray.com/eanlombardo/maven/ReactiveCaffeine/_latestVersion)

ReactiveCaffeine is a library that contains all sorts of things to make RXJava easier to use.

## Features
* DisposeBag
 * DisposeBag makes handling unsubscription of groups of Subscriptions easy. It allows you to add all of the Subscriptions and dispose of all of them easily. DisposeBag is re-usable and only holds on to subscriptions via weak references so you never have to worry about large subscribers living on and taking up valuable memory.
* ObservableBuilder
 * Sometimes you just need an Observable that emmits 6 items, sleeps 30 seconds and errors out. Normally you would have to use Observable.create and write all the code out for this. Or you can just use ObservableBuilder, which allows you to build Observables thati do exactly what you want. It's perfect for buildind up Observables that need to do something odd for testing purposes, or exploratory coding.

## Usage
Gradle
```Groovy
dependencies {
    compile 'reactive-caffeine:reactive-caffeine:1.0.0'
}
```
Maven
```XML
<dependency>
  <groupId>reactive-caffeine</groupId>
  <artifactId>reactive-caffeine</artifactId>
  <version>1.0.0</version>
</dependency>
```

ReactiveCaffeine should work just fine on Java 7, or Android from API levels 8 and up
