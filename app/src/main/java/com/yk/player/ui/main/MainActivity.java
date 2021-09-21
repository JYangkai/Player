package com.yk.player.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yk.player.R;
import com.yk.player.mvp.BaseMvpActivity;
import com.yk.player.rxjava.Observable;
import com.yk.player.rxjava.Subscriber;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.fromCallable(new Observable.OnCallable<String>() {
                    @Override
                    public String call() {
                        Log.d(TAG, "call: 1");
                        return "Hello DIO";
                    }
                })
                        .map(new Observable.Function1<String, Integer>() {
                            @Override
                            public Integer call(String s) {
                                Log.d(TAG, "call: 2");
                                return 123;
                            }
                        })
                        .flatMap(new Observable.Function1<Integer, Observable<String>>() {
                            @Override
                            public Observable<String> call(Integer integer) {
                                Log.d(TAG, "call: 3");
                                return Observable.just("Hello JOJO");
                            }
                        })
                        .subscribeOnIo()
                        .observeOnUi()
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onNext(String s) {
                                Log.d(TAG, "onNext: " + s);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "onError: ", e);
                            }
                        });
            }
        });
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }
}