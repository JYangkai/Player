package com.yk.player.rxjava;

public interface Subscriber<T> {
    void onNext(T t);

    void onComplete();

    void onError(Exception e);
}
