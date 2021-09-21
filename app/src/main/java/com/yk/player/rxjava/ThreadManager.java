package com.yk.player.rxjava;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {

    private static volatile ThreadManager instance;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final ExecutorService ioThread;

    private ThreadManager() {
        ioThread = Executors.newSingleThreadExecutor();
    }

    public static ThreadManager getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }
        return instance;
    }

    public void postUi(Runnable runnable) {
        uiHandler.post(runnable);
    }

    public void postIo(Runnable runnable) {
        ioThread.execute(runnable);
    }
}
