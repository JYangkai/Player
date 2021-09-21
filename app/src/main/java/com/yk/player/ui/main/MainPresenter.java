package com.yk.player.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.yk.player.data.bean.Video;
import com.yk.player.mvp.BaseMvpPresenter;
import com.yk.player.rxjava.Observable;
import com.yk.player.rxjava.Subscriber;
import com.yk.player.ui.list.ListFragment;
import com.yk.player.utils.LocalMediaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainPresenter extends BaseMvpPresenter<IMainView> {
    private static final String TAG = "MainPresenter";

    private final Context context;

    private final List<String> titleList = new ArrayList<>();
    private final List<Fragment> fragmentList = new ArrayList<>();

    public MainPresenter(Context context) {
        this.context = context;
    }

    public void loadVideoFragment() {
        Observable.fromCallable(new Observable.OnCallable<List<Video>>() {
            @Override
            public List<Video> call() {
                return LocalMediaUtils.getAllVideo(context);
            }
        })
                .map(new Observable.Function1<List<Video>, Boolean>() {
                    @Override
                    public Boolean call(List<Video> list) {
                        Log.d(TAG, "call: list size:" + list.size());

                        Map<String, List<Video>> map = LocalMediaUtils.getVideoCategory(list);

                        if (map.isEmpty()) {
                            return false;
                        }

                        Log.d(TAG, "call: map size:" + map.size());

                        for (String key : map.keySet()) {
                            if (!titleList.contains(key)) {
                                titleList.add(key);
                                fragmentList.add(ListFragment.newInstance(map.get(key)));
                            }
                        }

                        return true;
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        Log.d(TAG, "onNext: " + success);
                        if (success) {
                            if (getView() != null) {
                                getView().onLoadVideoFragment(titleList, fragmentList);
                            }
                        }
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
}
