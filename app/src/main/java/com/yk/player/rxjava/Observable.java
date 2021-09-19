package com.yk.player.rxjava;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Observable<T> {
    private final OnSubscribe<T> onSubscribe;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final ExecutorService ioService = Executors.newSingleThreadExecutor();

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public void subscribe(Subscriber<T> subscriber) {
        onSubscribe.call(subscriber);
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public static <T> Observable<T> from(T t) {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                subscriber.onNext(t);
                subscriber.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(T t) {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                subscriber.onNext(t);
            }
        });
    }

    public static <T> Observable<T> fromCallable(OnCallable<T> onCallable) {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                T t = onCallable.call();
                subscriber.onNext(t);
                subscriber.onComplete();
            }
        });
    }

    public Observable<T> nullMap() {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    public <R> Observable<R> map(Function1<T, R> function) {
        return new Observable<>(new OnSubscribe<R>() {
            @Override
            public void call(Subscriber<R> subscriber) {
                subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T t) {
                        R r = function.call(t);
                        subscriber.onNext(r);
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    public <R> Observable<R> flatMap(Function1<T, Observable<R>> function) {
        return new Observable<>(new OnSubscribe<R>() {
            @Override
            public void call(Subscriber<R> subscriber) {
                subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T t) {
                        Observable<R> r = function.call(t);
                        r.subscribe(subscriber);
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    public Observable<T> subscribeOn() {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                ioService.execute(new Runnable() {
                    @Override
                    public void run() {
                        subscribe(new Subscriber<T>() {
                            @Override
                            public void onNext(T t) {
                                subscriber.onNext(t);
                            }

                            @Override
                            public void onComplete() {
                                subscriber.onComplete();
                            }

                            @Override
                            public void onError(Exception e) {
                                subscriber.onError(e);
                            }
                        });
                    }
                });
            }
        });
    }

    public Observable<T> observeOn() {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<T> subscriber) {
                subscribe(new Subscriber<T>() {
                    @Override
                    public void onNext(T t) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(t);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onComplete();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    public interface OnSubscribe<T> {
        void call(Subscriber<T> subscriber);
    }

    public interface Function1<T, R> {
        R call(T t);
    }

    public interface OnCallable<T> {
        T call();
    }
}
