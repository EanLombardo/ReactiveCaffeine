# ReactiveCaffeine
[![Build Status](https://travis-ci.org/EanLombardo/ReactiveCaffeine.svg?branch=master)](https://travis-ci.org/EanLombardo/ReactiveCaffeine)[ ![Download](https://api.bintray.com/packages/eanlombardo/maven/ReactiveCaffeine/images/download.svg) ](https://bintray.com/eanlombardo/maven/ReactiveCaffeine/_latestVersion)[![License](http://img.shields.io/:license-apache-blue.svg?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0.html)

ReactiveCaffeine is a library that contains all sorts of things to make RXJava easier to use.

ReactiveCaffeine is code meant for production purposes, for tests you should take a look at [ReactiveCaffeineTesting](https://github.com/EanLombardo/ReactiveCaffeineTesting)

## Features
* DisposeBag - [Javadoc](http://eanlombardo.github.io/ReactiveCaffeine/com/rxc/DisposeBag.html) - [Wiki](https://github.com/EanLombardo/ReactiveCaffeine/wiki/DisposeBag)
  * DisposeBag makes handling unsubscription of groups of Subscriptions easy. It allows you to add all of the Subscriptions and dispose of all of them easily. DisposeBag is re-usable and only holds on to subscriptions via weak references so you never have to worry about large subscribers living on and taking up valuable memory.
* ObservableBuilder - [Javadoc](http://eanlombardo.github.io/ReactiveCaffeine/com/rxc/ObservableBuilder.html) - [Wiki](https://github.com/EanLombardo/ReactiveCaffeine/wiki/ObservableBuilder)
  * Sometimes you just need an Observable that emits 6 items, sleeps 30 seconds and errors out. Normally you would have to use Observable.create and write all the code out for this. Or you can just use ObservableBuilder, which allows you to build Observables thati do exactly what you want. It's perfect for building up Observables that need to do something odd for testing purposes, or exploratory coding.

For more information on using ReactiveCaffeineTesting see the [Javadoc](http://eanlombardo.github.io/ReactiveCaffeine/) or the [Wiki](https://github.com/EanLombardo/ReactiveCaffeine/wiki)

## Usage
Gradle
```Groovy
repositories {
    jcenter()
}
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
